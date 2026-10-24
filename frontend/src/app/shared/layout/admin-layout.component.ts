import { Component, computed, inject, signal } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { DestroyRef } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { HotelDemoStore } from '../../core/demo/hotel-demo.store';
import { HotelAdminStore } from '../../core/demo/hotel-admin.store';
import { ThemeService } from '../../core/services/theme.service';
import { ModalFocusDirective } from '../components/modal-focus.directive';
import { AuthService } from '../../core/services/auth/auth.service';

@Component({
  imports: [RouterOutlet, RouterLink, RouterLinkActive, ReactiveFormsModule, ModalFocusDirective],
  selector: 'app-admin-layout',
  host: { '(document:keydown.escape)': 'menuOpen.set(false)' },
  styleUrl: './admin-layout.component.scss',
  templateUrl: './admin-layout.component.html',
})
export class AdminLayoutComponent {
  constructor() {
    // Restore shift labels after a full page refresh as well as after login.
    void this.auth.loadTurnos().catch(() => undefined);
    if (typeof matchMedia !== 'undefined') {
      const media = matchMedia('(max-width: 1080px)');
      const update = () => this.compact.set(media.matches);
      update();
      media.addEventListener('change', update);
      inject(DestroyRef).onDestroy(() => media.removeEventListener('change', update));
    }
  }
  protected readonly auth = inject(AuthService);
  private readonly router = inject(Router);
  protected readonly profileOpen = signal(false);
  protected readonly profileDetails = signal(false);
  protected readonly initials = computed(() =>
    (this.auth.currentUser()?.nombres ?? 'Usuario')
      .split(' ')
      .map((part) => part[0])
      .slice(0, 2)
      .join('')
      .toUpperCase(),
  );
  protected readonly store = inject(HotelDemoStore);
  protected readonly admin = inject(HotelAdminStore);
  protected readonly theme = inject(ThemeService);
  private readonly fb = inject(FormBuilder);
  protected readonly menuOpen = signal(false);
  protected readonly compact = signal(false);
  protected readonly navQuery = signal('');
  protected readonly settingsOpen = signal(false);
  protected readonly settingsError = signal('');
  protected readonly settingsForm = this.fb.nonNullable.group({
    name: ['', Validators.required],
    ruc: [''],
    currency: ['PEN', Validators.required],
  });
  protected openSettings() {
    this.settingsForm.setValue(this.admin.profile());
    this.settingsError.set('');
    this.menuOpen.set(false);
    this.settingsOpen.set(true);
  }
  protected saveSettings() {
    if (this.settingsForm.invalid) {
      this.settingsError.set('Escriba el nombre del hotel.');
      return;
    }
    const value = this.settingsForm.getRawValue();
    if (value.ruc && !/^\d{11}$/.test(value.ruc)) {
      this.settingsError.set('El RUC debe tener 11 dígitos.');
      return;
    }
    this.admin.profile.set({ name: value.name.trim(), ruc: value.ruc, currency: value.currency });
    this.settingsOpen.set(false);
  }
  protected readonly navigation = [
    { path: '/inicio', label: 'Inicio', icon: 'pi pi-home' },
    { path: '/dashboard', label: 'Resumen general', icon: 'pi pi-chart-bar' },
    { path: '/recepcion', label: 'Recepción', icon: 'pi pi-th-large' },
    { path: '/reservas', label: 'Reservas', icon: 'pi pi-calendar' },
    { path: '/habitaciones', label: 'Habitaciones', icon: 'pi pi-building' },
    { path: '/huespedes', label: 'Huéspedes', icon: 'pi pi-users' },
    { path: '/servicios', label: 'Servicios', icon: 'pi pi-bell' },
    { path: '/finanzas', label: 'Finanzas', icon: 'pi pi-wallet' },
    { path: '/personal', label: 'Personal', icon: 'pi pi-id-card' },
  ];
  protected readonly filteredNavigation = computed(() =>
    this.navigation.filter(
      (item) =>
        this.auth.canAccess(item.path.slice(1)) &&
        item.label.toLocaleLowerCase('es').includes(this.navQuery().trim().toLocaleLowerCase('es')),
    ),
  );
  protected setNavQuery(event: Event) {
    this.navQuery.set((event.target as HTMLInputElement).value);
  }
  protected logout() {
    this.auth.logout();
    this.router.navigateByUrl('/login');
  }
}
