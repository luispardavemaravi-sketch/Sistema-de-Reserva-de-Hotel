import { Component, computed, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { DemoGuest, HotelDemoStore } from '../../core/demo/hotel-demo.store';
import { ModalFocusDirective } from '../../shared/components/modal-focus.directive';

@Component({
  selector: 'app-huespedes',
  imports: [ReactiveFormsModule, ModalFocusDirective],
  template: ` <section class="page">
    <div class="page-head">
      <div>
        <h1>Huéspedes</h1>
        <p>Directorio de personas y datos de contacto.</p>
      </div>
      <button class="btn btn-primary" (click)="openNew()">
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
            <tr>
              <td>
                <strong>{{ guest.name }}</strong>
              </td>
              <td class="mono">{{ guest.document }}</td>
              <td>{{ guest.phone }}</td>
              <td>{{ guest.email }}</td>
              <td class="num">{{ guest.stays }}</td>
              <td>
                <button class="btn" (click)="openEdit(guest)">Editar</button>
                <button class="btn btn-danger" (click)="askDelete(guest)">Eliminar</button>
              </td>
            </tr>
          } @empty {
            <tr>
              <td colspan="6" class="empty">No se encontraron huéspedes.</td>
            </tr>
          }
        </tbody>
      </table>
    </div>
    @if (dialogOpen()) {
      <div class="dialog-backdrop" (click)="dialogOpen.set(false)">
        <section
          class="dialog"
          role="dialog"
          aria-modal="true"
          aria-labelledby="guest-dialog-title"
          (click)="$event.stopPropagation()"
        >
          <div class="dialog-head">
            <h2 id="guest-dialog-title">{{ editing() ? 'Editar huésped' : 'Nuevo huésped' }}</h2>
            <button class="icon-btn" aria-label="Cerrar" (click)="dialogOpen.set(false)">
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
              <button type="button" class="btn" (click)="dialogOpen.set(false)">Cancelar</button
              ><button type="submit" class="btn btn-primary">Guardar huésped</button>
            </div>
          </form>
        </section>
      </div>
    }
    @if (deleting(); as guest) {
      <div class="dialog-backdrop" (click)="deleting.set(null)">
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
            <button class="btn" (click)="deleting.set(null)">Cancelar</button
            ><button class="btn btn-danger" (click)="deleteGuest(guest)">Eliminar</button>
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
  private readonly fb = inject(FormBuilder);
  readonly query = signal('');
  readonly dialogOpen = signal(false);
  readonly editing = signal<DemoGuest | null>(null);
  readonly deleting = signal<DemoGuest | null>(null);
  readonly error = signal('');
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
  save() {
    if (this.form.invalid) {
      this.error.set('Complete los campos y escriba un correo válido.');
      return;
    }
    const value = this.form.getRawValue();
    const editing = this.editing();
    if (editing) {
      this.store.guests.update((rows) =>
        rows.map((g) => (g.id === editing.id ? { ...g, ...value } : g)),
      );
    } else {
      this.store.guests.update((rows) => [
        ...rows,
        { id: Math.max(...rows.map((g) => g.id)) + 1, ...value, stays: 0 },
      ]);
    }
    this.dialogOpen.set(false);
  }
  deleteGuest(guest: DemoGuest) {
    this.store.guests.update((rows) => rows.filter((g) => g.id !== guest.id));
    this.deleting.set(null);
  }
}
