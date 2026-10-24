import { LoadingSkeletonComponent } from '../../shared/components/loading-skeleton.component';
import { Component, computed, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { DemoGuest, HotelDemoStore } from '../../core/demo/hotel-demo.store';
import { HuespedService } from '../../core/services/huesped/huesped.service';
import { HuespedResponseDTO } from '../../core/models/huesped.model';
import { ReservaBackendService } from '../../core/services/reserva/reserva-backend.service';
import { firstValueFrom } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import { ModalFocusDirective } from '../../shared/components/modal-focus.directive';

@Component({
  selector: 'app-huespedes',
  imports: [LoadingSkeletonComponent, ReactiveFormsModule, ModalFocusDirective],
  template: ` <section class="page" [attr.aria-busy]="loading() || saving()">
  @if (notice()) { <p class="success-message" role="status" animate.enter="parda-row-enter"><i class="pi pi-check-circle" aria-hidden="true"></i>{{ notice() }}<button [disabled]="saving()" class="icon-btn" type="button" aria-label="Cerrar confirmación" (click)="notice.set('')"><i class="pi pi-times" aria-hidden="true"></i></button></p> }
    @if (loading()) { <app-loading-skeleton label="Cargando huespedes…" /> }
    @if (error() && !dialogOpen()) { <p class="error" role="alert">{{ error() }}</p> }
    @if (saving()) { <p role="status"><i class="pi pi-spin pi-spinner" aria-hidden="true"></i> Procesando solicitud…</p> }
  <div class="page-head">
      <div>
        <h1>Huéspedes</h1>
        <p>Directorio de personas y datos de contacto.</p>
      </div>
      <button [disabled]="saving()" class="btn btn-primary" (click)="openNew()">
        <i class="pi pi-plus" aria-hidden="true"></i> Nuevo huésped
      </button>
    </div>
    <div class="toolbar">
      <input
        class="field search"
        type="search"
        aria-label="Buscar huésped"
        placeholder="Buscar nombre o documento"
        (input)="setQuery($event)"
      /><span class="muted small">{{ filtered().length }} huéspedes registrados</span>
    </div>
    <div class="card table-wrap">
      <table class="data-table">
        <thead>
          <tr>
            <th>Huésped</th>
            <th>Documento</th>
            <th>Teléfono</th>
            <th>Correo</th>
            <th class="num">Estadías</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          @for (guest of filtered(); track guest.id) {
            <tr animate.enter="parda-row-enter">
              <td>
                <strong>{{ guest.name }}</strong>
              </td>
              <td class="mono">{{ guest.document }}</td>
              <td>{{ guest.phone }}</td>
              <td>{{ guest.email }}</td>
              <td class="num">{{ guest.stays }}</td>
              <td>
                <button [disabled]="saving()" class="btn" (click)="openEdit(guest)">Editar</button>
                <button [disabled]="saving()" class="btn btn-danger" (click)="askDelete(guest)">Eliminar</button>
              </td>
            </tr>
          } @empty {
            <tr animate.enter="parda-row-enter">
              <td colspan="6" class="empty">No se encontraron huéspedes.</td>
            </tr>
          }
        </tbody>
      </table>
    </div>
    @if (dialogOpen()) {
      <div class="dialog-backdrop" animate.enter="parda-dialog-enter" animate.leave="parda-dialog-leave" (click)="!saving() && dialogOpen.set(false)">
        <section
          class="dialog"
          role="dialog"
          aria-modal="true"
          aria-labelledby="guest-dialog-title"
          (click)="$event.stopPropagation()"
        >
          <div class="dialog-head">
            <h2 id="guest-dialog-title">{{ editing() ? 'Editar huésped' : 'Nuevo huésped' }}</h2>
            <button [disabled]="saving()" class="icon-btn" aria-label="Cerrar" (click)="!saving() && dialogOpen.set(false)">
              <i class="pi pi-times" aria-hidden="true"></i>
            </button>
          </div>
          <form [formGroup]="form" (ngSubmit)="save()">
            <div class="form-grid">
              <label class="form-field full"
                >Nombre completo<input class="field" formControlName="name" required /></label
              ><label class="form-field"
                >Documento<input class="field" formControlName="document" required /></label
              ><label class="form-field"
                >Teléfono<input class="field" formControlName="phone" required /></label
              ><label class="form-field full"
                >Correo electrónico<input
                  class="field"
                  type="email"
                  formControlName="email"
                  required
              /></label>
            </div>
            @if (error()) {
              <p class="error" role="alert">{{ error() }}</p>
            }
            <div class="dialog-actions">
              <button [disabled]="saving()" type="button" class="btn" (click)="!saving() && dialogOpen.set(false)">Cancelar</button
              ><button [disabled]="saving()" type="submit" class="btn btn-primary">{{ saving() ? 'Guardando…' : 'Guardar huésped' }}</button>
            </div>
          </form>
        </section>
      </div>
    }
    @if (deleting(); as guest) {
      <div class="dialog-backdrop" animate.enter="parda-dialog-enter" animate.leave="parda-dialog-leave" (click)="!saving() && deleting.set(null)">
        <section
          class="dialog"
          role="alertdialog"
          aria-modal="true"
          aria-labelledby="delete-guest-title"
          aria-describedby="delete-guest-description"
          (click)="$event.stopPropagation()"
        >
          <h2 id="delete-guest-title">Eliminar huésped</h2>
          <p id="delete-guest-description">
            Se quitará a {{ guest.name }} del directorio del hotel.
          </p>
          <div class="dialog-actions">
            <button [disabled]="saving()" class="btn" (click)="!saving() && deleting.set(null)">Cancelar</button
            ><button [disabled]="saving()" class="btn btn-danger" (click)="deleteGuest(guest)">Eliminar</button>
          </div>
        </section>
      </div>
    }
  </section>`,
  styles: [
    `
      .error {
        color: #a1272b;
        font-size: 13px;
      }
    `,
  ],
})
export class HuespedesComponent {
  readonly store = inject(HotelDemoStore);
  private readonly api = inject(HuespedService);
  private readonly reservasApi = inject(ReservaBackendService);
  private readonly fb = inject(FormBuilder);
  readonly query = signal('');
  readonly dialogOpen = signal(false);
  readonly editing = signal<DemoGuest | null>(null);
  readonly deleting = signal<DemoGuest | null>(null);
  readonly error = signal('');
  readonly loading = signal(true);
  readonly saving = signal(false);
  readonly notice = signal('');
  constructor() { void this.refresh(); }
  private toView(guest: HuespedResponseDTO): DemoGuest {
    return { id: guest.idHuesped, name: `${guest.nombre} ${guest.apellidos}`.trim(),
      document: guest.documentoIdentidad, phone: guest.telefono || '',
      email: guest.email, stays: 0 };
  }
  private async refresh() {
    this.loading.set(true);
    this.store.guests.set([]);
    try {
      const [guests, reservations] = await Promise.all([
        firstValueFrom(this.api.getAll()), firstValueFrom(this.reservasApi.reservations()),
      ]);
      this.store.guests.set(guests.map((guest) => ({ ...this.toView(guest),
        stays: reservations.filter((row) => row.idHuesped === guest.idHuesped &&
          (row.estadoReserva === 'En curso' || row.estadoReserva === 'Finalizada')).length })));
      this.error.set('');
    } catch (error) { this.error.set(this.apiError(error)); }
    finally { this.loading.set(false); }
  }
  private apiError(error: unknown): string {
    if (error instanceof HttpErrorResponse)
      return error.error?.detail || `No se pudo completar la operación (${error.status}).`;
    return 'No se pudo conectar con el servidor.';
  }
  readonly form = this.fb.nonNullable.group({
    name: ['', Validators.required],
    document: ['', Validators.required],
    phone: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
  });
  readonly filtered = computed(() =>
    this.store
      .guests()
      .filter((g) => `${g.name} ${g.document}`.toLowerCase().includes(this.query().toLowerCase())),
  );
  setQuery(event: Event) {
    this.query.set((event.target as HTMLInputElement).value);
  }
  openNew() {
    this.editing.set(null);
    this.form.reset();
    this.error.set('');
    this.dialogOpen.set(true);
  }
  openEdit(guest: DemoGuest) {
    this.editing.set(guest);
    this.form.setValue({
      name: guest.name,
      document: guest.document,
      phone: guest.phone,
      email: guest.email,
    });
    this.error.set('');
    this.dialogOpen.set(true);
  }
  askDelete(guest: DemoGuest) {
    this.deleting.set(guest);
  }
  async save() {
    if (this.saving()) return;
    if (this.form.invalid) {
      this.error.set('Complete los campos y escriba un correo válido.');
      return;
    }
    const value = this.form.getRawValue();
    const editing = this.editing();
    const parts = value.name.trim().split(/\s+/);
    if (parts.length < 2) {
      this.error.set('Escriba nombre y apellido del huésped.');
      return;
    }
    const body = { nombre: parts.slice(0, -1).join(' '), apellidos: parts.at(-1)!,
      documentoIdentidad: value.document, email: value.email, telefono: value.phone };
    this.saving.set(true);
    this.notice.set('');
    this.error.set('');
    try {
      await firstValueFrom(editing ? this.api.update(editing.id, body) : this.api.create(body));
      this.dialogOpen.set(false);
      this.notice.set('Huésped guardado por el servidor.');
      await this.refresh();
    } catch (error) { this.error.set(this.apiError(error)); }
    finally { this.saving.set(false); }
  }
  async deleteGuest(guest: DemoGuest) {
    if (this.saving()) return;
    this.saving.set(true);
    this.notice.set('');
    this.error.set('');
    try {
      await firstValueFrom(this.api.delete(guest.id));
      this.deleting.set(null);
      this.notice.set('Huésped eliminado por el servidor.');
      await this.refresh();
    } catch (error) { this.error.set(this.apiError(error)); }
    finally { this.saving.set(false); }
  }
}
