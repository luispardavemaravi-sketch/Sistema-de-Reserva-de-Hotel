import { Component, computed, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { HotelAdminStore } from '../../core/demo/hotel-admin.store';
import { DemoReservation, DemoRoom, HotelDemoStore } from '../../core/demo/hotel-demo.store';
import { ModalFocusDirective } from '../../shared/components/modal-focus.directive';
import { firstValueFrom } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import { BackendGuest, BackendReservation, BackendRoom, ReservaBackendService } from '../../core/services/reserva/reserva-backend.service';

type ReservationDialog = 'form' | 'companions' | 'extras' | 'delete' | null;

@Component({
  selector: 'app-reservas',
  imports: [ReactiveFormsModule, ModalFocusDirective],
  templateUrl: './reservas.component.html',
  styles: [
    `
      .reservation-table {
        min-width: 1030px;
      }
      .reservation-actions {
        display: flex;
        gap: 5px;
        flex-wrap: wrap;
      }
      .reservation-actions .btn {
        font-size: 11px;
        min-height: 28px;
        padding: 4px 7px;
      }
      .mode-tag {
        font-size: 11px;
        color: var(--muted, #677782);
        display: block;
        margin-top: 3px;
      }
      .extras {
        border: 0;
        padding: 0;
        display: flex;
        flex-wrap: wrap;
        gap: 12px;
      }
      .extras legend {
        font-size: 13px;
        font-weight: 600;
        margin-bottom: 8px;
      }
      .extras label {
        font-size: 13px;
      }
      .error {
        color: #ad353b;
        font-size: 13px;
      }
      .large-dialog {
        max-width: 650px;
      }
      .companion-list {
        display: grid;
        gap: 8px;
        margin-bottom: 14px;
      }
      .companion-list > div {
        display: flex;
        justify-content: space-between;
        align-items: center;
        gap: 10px;
        border-bottom: 1px solid var(--line, #e1e8eb);
        padding-bottom: 8px;
      }
      .inline-note {
        font-size: 12px;
        color: var(--muted, #60717b);
      }
      @media (max-width: 650px) {
        .table-wrap {
          overflow: visible;
        }
        .reservation-table,
        .reservation-table tbody,
        .reservation-table tr,
        .reservation-table td {
          display: block;
          width: 100%;
          min-width: 0;
        }
        .reservation-table thead {
          display: none;
        }
        .reservation-table tr {
          padding: 13px 0;
          border-top: 1px solid var(--line, #e1e8eb);
        }
        .reservation-table td {
          border: 0;
          padding: 4px 0;
        }
        .reservation-actions {
          margin-top: 7px;
        }
        .reservation-actions .btn {
          flex: 1;
        }
        .reservation-table td.num {
          text-align: left;
        }
      }
    `,
  ],
})
export class ReservasComponent {
  readonly store = inject(HotelDemoStore);
  readonly admin = inject(HotelAdminStore);
  private readonly api = inject(ReservaBackendService);
  private backendRooms: BackendRoom[] = [];
  private backendGuests: BackendGuest[] = [];
  readonly loading = signal(true);
  private readonly fb = inject(FormBuilder);
  readonly tabs = ['Todas', 'Pendiente', 'Confirmada', 'En curso', 'Finalizada', 'Cancelada'];
  readonly filter = signal('Todas');
  readonly query = signal('');
  readonly dialog = signal<ReservationDialog>(null);
  readonly selectedId = signal<number | null>(null);
  readonly selected = computed(
    () => this.store.reservations().find((row) => row.id === this.selectedId()) || null,
  );
  readonly error = signal('');
  constructor() { void this.refresh(); }

  private async refresh() {
    this.loading.set(true);
    this.store.reservations.set([]);
    this.store.rooms.set([]);
    this.store.guests.set([]);
    try {
      const [rooms, guests, reservations] = await Promise.all([
        firstValueFrom(this.api.rooms()), firstValueFrom(this.api.guests()),
        firstValueFrom(this.api.reservations()),
      ]);
      this.backendRooms = rooms;
      this.backendGuests = guests;
      this.store.rooms.set(rooms.map((room) => ({
        id: room.id, number: room.numeroHabitacion, floor: 1,
        type: room.tipoHabitacion || 'Estándar',
        status: (!room.activo ? 'Mantenimiento' :
          reservations.some((row) => row.idHabitacion === room.id && row.estadoReserva === 'En curso')
            ? 'Ocupada' : 'Disponible') as DemoRoom['status'],
        rate: Number(room.precioActual),
      })));
      this.store.guests.set(guests.map((guest) => ({
        id: guest.idHuesped, name: `${guest.nombre} ${guest.apellidos}`.trim(),
        document: guest.documentoIdentidad, phone: guest.telefono,
        email: guest.email, stays: 0,
      })));
      this.store.reservations.set(reservations.map((row) => this.toView(row)));
      this.error.set('');
    } catch (error) {
      this.error.set(this.apiError(error));
    } finally {
      this.loading.set(false);
    }
  }

  private toView(row: BackendReservation): DemoReservation {
    const room = this.backendRooms.find((item) => item.id === row.idHabitacion);
    const guest = this.backendGuests.find((item) => item.idHuesped === row.idHuesped);
    return { id: row.idReserva, code: row.codigoReserva,
      guest: guest ? `${guest.nombre} ${guest.apellidos}`.trim() : `Huésped #${row.idHuesped}`,
      room: room?.numeroHabitacion ?? `#${row.idHabitacion}`,
      from: row.fechaEntradaPlanificada, to: row.fechaSalidaPlanificada,
      status: row.estadoReserva, extras: [], total: Number(row.montoTotalEstimado), mode: 'Noche' };
  }

  private apiError(error: unknown): string {
    if (error instanceof HttpErrorResponse)
      return error.error?.detail || `No se pudo guardar o cargar la reserva (${error.status}).`;
    return 'No se pudo conectar con el servidor.';
  }
  readonly form = this.fb.nonNullable.group({
    guest: ['', Validators.required],
    room: ['', Validators.required],
    mode: ['Noche'],
    startDate: ['', Validators.required],
    startTime: ['14:00'],
    endDate: [''],
    durationHours: [3],
    breakfast: [false],
    parking: [false],
    spa: [false],
  });
  readonly companionForm = this.fb.nonNullable.group({
    name: ['', Validators.required],
    document: ['', Validators.required],
    relationship: ['Familiar'],
  });
  readonly extraForm = this.fb.nonNullable.group({
    itemId: [0, Validators.required],
    quantity: [1, [Validators.required, Validators.min(1)]],
  });
  readonly filtered = computed(() =>
    this.store
      .reservations()
      .filter(
        (row) =>
          (this.filter() === 'Todas' || row.status === this.filter()) &&
          `${row.guest} ${row.code} ${row.room}`.toLowerCase().includes(this.query().toLowerCase()),
      ),
  );
  availableRooms() {
    const start = this.form.controls.startDate.value;
    const end = this.form.controls.endDate.value;
    return this.store.rooms().filter((room) => {
      if (room.status === 'Mantenimiento') return false;
      if (!start || !end || end <= start) return true;
      return !this.store.reservations().some((row) => row.id !== this.selectedId() &&
        row.room === room.number && row.status !== 'Cancelada' &&
        row.status !== 'Finalizada' && row.from < end && row.to > start);
    });
  }
  readonly activeCount = computed(
    () =>
      this.store
        .reservations()
        .filter((row) => row.status === 'Confirmada' || row.status === 'En curso').length,
  );
  readonly pendingCount = computed(
    () => this.store.reservations().filter((row) => row.status === 'Pendiente').length,
  );
  readonly hoursCount = computed(
    () =>
      this.store.reservations().filter((row) => row.mode === 'Horas' && row.status !== 'Cancelada')
        .length,
  );
  readonly extrasCount = computed(
    () =>
      this.store.reservations().filter((row) => row.extras.length && row.status !== 'Cancelada')
        .length,
  );
  setQuery(event: Event) {
    this.query.set((event.target as HTMLInputElement).value);
  }
  shortDate(date: string) {
    if (!date) return '—';
    const [year, month, day] = date.split('-');
    return `${day}/${month}/${year.slice(2)}`;
  }
  badgeClass(status: string) {
    return `badge ${status === 'En curso' ? 'badge-blue' : status === 'Confirmada' ? 'badge-green' : status === 'Pendiente' ? 'badge-amber' : 'badge-gray'}`;
  }
  stayLabel(row: DemoReservation) {
    return row.mode === 'Horas'
      ? `${row.durationHours || 3} h · ${row.startAt ? new Intl.DateTimeFormat('es-PE', { hour: '2-digit', minute: '2-digit' }).format(new Date(row.startAt)) : this.shortDate(row.from)}`
      : `${this.shortDate(row.from)} – ${this.shortDate(row.to)}`;
  }
  openForm(row?: DemoReservation) {
    this.selectedId.set(row?.id || null);
    this.error.set('');
    const today = new Date().toISOString().slice(0, 10);
    this.form.setValue({
      guest: row?.guest || '',
      room: row?.room || '',
      mode: row?.mode || 'Noche',
      startDate: row?.from || today,
      startTime: row?.startAt ? new Date(row.startAt).toTimeString().slice(0, 5) : '14:00',
      endDate: row?.to || today,
      durationHours: row?.durationHours || 3,
      breakfast: row?.extras.includes('Desayuno') || false,
      parking: row?.extras.includes('Cochera') || false,
      spa: row?.extras.includes('Spa') || false,
    });
    this.dialog.set('form');
  }
  roomPrice() {
    const v = this.form.getRawValue();
    const room = this.store.rooms().find((item) => item.number === v.room);
    if (!room) return 0;
    const rates = this.admin.ratesFor(room);
    if (v.mode === 'Horas')
      return v.durationHours === 2
        ? rates.hours2
        : v.durationHours === 4
          ? rates.hours4
          : v.durationHours === 6
            ? rates.hours6
            : rates.hours3;
    const nights = Math.max(
      1,
      Math.round((Date.parse(v.endDate) - Date.parse(v.startDate)) / 86_400_000),
    );
    return nights * rates.night24;
  }
  estimatedTotal() {
    const v = this.form.getRawValue();
    return this.roomPrice() + (v.breakfast ? 28 : 0) + (v.parking ? 30 : 0) + (v.spa ? 130 : 0);
  }
  async save() {
    if (this.form.invalid) {
      this.error.set('Complete huésped, habitación y fecha.');
      return;
    }
    const v = this.form.getRawValue();
    if (v.mode === 'Horas') {
      this.error.set('Las reservas por horas aún no están disponibles en la API.');
      return;
    }
    if (v.breakfast || v.parking || v.spa) {
      this.error.set('Los extras aún no están disponibles para reservas en la API.');
      return;
    }
    if (v.mode === 'Noche' && v.endDate <= v.startDate) {
      this.error.set('La salida debe ser posterior a la entrada.');
      return;
    }
    const start = new Date(`${v.startDate}T${v.startTime}:00`);
    if (Number.isNaN(start.getTime())) {
      this.error.set('Revise la fecha y hora de entrada.');
      return;
    }
    const id = this.selectedId();
    const guestId = this.store.guests().find((guest) => guest.name === v.guest)?.id;
    const roomId = this.store.rooms().find((room) => room.number === v.room)?.id;
    if (!guestId || !roomId) {
      this.error.set('Seleccione un huésped y una habitación existentes.');
      return;
    }
    try {
      const body = { idHuesped: guestId, idHabitacion: roomId,
        fechaEntradaPlanificada: v.startDate, fechaSalidaPlanificada: v.endDate };
      await firstValueFrom(id === null ? this.api.create(body) : this.api.update(id, body));
      this.dialog.set(null);
      await this.refresh();
    } catch (error) { this.error.set(this.apiError(error)); }
  }
  async confirm(row: DemoReservation) {
    try { await firstValueFrom(this.api.transition(row.id, 'confirmacion')); await this.refresh(); }
    catch (error) { this.error.set(this.apiError(error)); }
  }
  async checkIn(row: DemoReservation) {
    try { await firstValueFrom(this.api.transition(row.id, 'entrada')); await this.refresh(); }
    catch (error) { this.error.set(this.apiError(error)); }
  }
  async checkOut(row: DemoReservation) {
    try { await firstValueFrom(this.api.transition(row.id, 'salida')); await this.refresh(); }
    catch (error) { this.error.set(this.apiError(error)); }
  }
  openCompanions(row: DemoReservation) {
    this.selectedId.set(row.id);
    this.companionForm.reset();
    this.dialog.set('companions');
    this.error.set('');
  }
  companions(row: DemoReservation) {
    return row.companions || (row.companion ? [row.companion] : []);
  }
  addCompanion() {
    this.error.set('Los acompañantes aún no están disponibles en la API.');
  }
  removeCompanion(_index: number) {
    this.error.set('Los acompañantes aún no están disponibles en la API.');
  }
  openExtras(row: DemoReservation) {
    this.selectedId.set(row.id);
    this.extraForm.setValue({ itemId: 0, quantity: 1 });
    this.dialog.set('extras');
    this.error.set('');
  }
  addExtra() {
    this.error.set('Los extras aún no están disponibles en la API.');
  }
  askDelete(row: DemoReservation) {
    this.selectedId.set(row.id);
    this.dialog.set('delete');
  }
  async deleteSelected() {
    const row = this.selected();
    if (!row) return;
    try { await firstValueFrom(this.api.cancel(row.id)); this.dialog.set(null); await this.refresh(); }
    catch (error) { this.error.set(this.apiError(error)); }
  }
  async cancel(row: DemoReservation) {
    try { await firstValueFrom(this.api.cancel(row.id)); await this.refresh(); }
    catch (error) { this.error.set(this.apiError(error)); }
  }
}
