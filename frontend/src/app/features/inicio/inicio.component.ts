import { Component, computed, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth/auth.service';
import { HotelAdminStore } from '../../core/demo/hotel-admin.store';

@Component({
  selector: 'app-inicio',
  imports: [RouterLink],
  templateUrl: './inicio.component.html',
  styleUrl: './inicio.component.scss',
})
export class InicioComponent {
  protected readonly auth = inject(AuthService);
  protected readonly admin = inject(HotelAdminStore);
  protected readonly greeting = (() => {
    const hour = Number(new Intl.DateTimeFormat('es-PE', {
      timeZone: 'America/Lima', hour: 'numeric', hourCycle: 'h23',
    }).format(new Date()));
    return hour < 12 ? 'Buenos días' : hour < 19 ? 'Buenas tardes' : 'Buenas noches';
  })();
  protected readonly shortcuts = computed(() => [
    { path: 'dashboard', title: 'Resumen general', description: 'La actividad de tu hotel', icon: 'pi-chart-bar' },
    { path: 'recepcion', title: 'Recepción', description: 'Gestiona las estancias', icon: 'pi-th-large' },
    { path: 'reservas', title: 'Reservas', description: 'Organiza las próximas visitas', icon: 'pi-calendar' },
    { path: 'habitaciones', title: 'Habitaciones', description: 'Consulta disponibilidad', icon: 'pi-building' },
    { path: 'huespedes', title: 'Huéspedes', description: 'Conoce a tus visitantes', icon: 'pi-users' },
    { path: 'servicios', title: 'Servicios', description: 'Atiende cada detalle', icon: 'pi-bell' },
    { path: 'finanzas', title: 'Finanzas', description: 'Consulta caja y movimientos', icon: 'pi-wallet' },
    { path: 'personal', title: 'Personal', description: 'Organiza a tu equipo', icon: 'pi-id-card' },
  ].filter(item => this.auth.canAccess(item.path)));
}
