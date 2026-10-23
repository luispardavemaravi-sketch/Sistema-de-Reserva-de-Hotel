import { Component, computed, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { HotelAdminStore } from '../../core/demo/hotel-admin.store';
import { DemoReservation, HotelDemoStore } from '../../core/demo/hotel-demo.store';
import { ModalFocusDirective } from '../../shared/components/modal-focus.directive';

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
  save() {
    if (this.form.invalid) {
      this.error.set('Complete huésped, habitación y fecha.');
      return;
    }
    const v = this.form.getRawValue();
    if (v.mode === 'Noche' && v.endDate <= v.startDate) {
      this.error.set('La salida debe ser posterior a la entrada.');
      return;
    }
    const start = new Date(`${v.startDate}T${v.startTime}:00`);
    if (Number.isNaN(start.getTime())) {
      this.error.set('Revise la fecha y hora de entrada.');
      return;
    }
    const end =
      v.mode === 'Horas'
        ? new Date(start.getTime() + v.durationHours * 3_600_000)
        : new Date(`${v.endDate}T${v.startTime}:00`);
    const id = this.selectedId();
    const current = this.selected();
    const extras = [v.breakfast && 'Desayuno', v.parking && 'Cochera', v.spa && 'Spa'].filter(
      (item): item is string => typeof item === 'string',
    );
    const record: DemoReservation = {
      id: id ?? Math.max(0, ...this.store.reservations().map((row) => row.id)) + 1,
      code:
        current?.code ||
        `RS-${new Date().getFullYear()}-${String(this.store.reservations().length + 41).padStart(3, '0')}`,
      guest: v.guest,
      room: v.room,
      from: v.startDate,
      to: end.toISOString().slice(0, 10),
      status: current?.status || 'Pendiente',
      mode: v.mode as 'Horas' | 'Noche',
      durationHours:
        v.mode === 'Horas'
          ? v.durationHours
          : Math.round((end.getTime() - start.getTime()) / 3_600_000),
      startAt: start.toISOString(),
      endAt: end.toISOString(),
      extras,
      total: this.estimatedTotal(),
      companion: current?.companion,
      companions: current?.companions,
    };
    this.store.reservations.update((rows) =>
      id === null ? [record, ...rows] : rows.map((row) => (row.id === id ? record : row)),
    );
    this.dialog.set(null);
  }
  confirm(row: DemoReservation) {
    this.store.updateReservation(row.id, 'Confirmada');
  }
  checkIn(row: DemoReservation) {
    const room = this.store.rooms().find((item) => item.number === row.room);
    if (!room || room.status !== 'Disponible') {
      this.error.set('La habitación no está disponible para el check-in.');
      return;
    }
    const startedAt = new Date();
    const durationHours = row.durationHours || 24;
    const endsAt = new Date(startedAt.getTime() + durationHours * 3_600_000);
    this.store.startStay(room.id, row.guest, {
      mode: row.mode || 'Noche',
      durationHours,
      startedAt: startedAt.toISOString(),
      endsAt: endsAt.toISOString(),
      paymentMethod: this.admin.paymentMethods()[0] || 'Efectivo',
    });
    this.store.reservations.update((rows) =>
      rows.map((item) =>
        item.id === row.id
          ? {
              ...item,
              status: 'En curso',
              startAt: startedAt.toISOString(),
              endAt: endsAt.toISOString(),
            }
          : item,
      ),
    );
    this.admin.addLedger({
      description: `Reserva ${row.code} · habitación ${room.number}`,
      kind: 'Alojamiento',
      amount: row.total,
      method: this.admin.paymentMethods()[0] || 'Efectivo',
      room: room.number,
      roomType: room.type,
      stayMode: row.mode || 'Noche',
      sourceId: row.id,
    });
    this.error.set('');
  }
  checkOut(row: DemoReservation) {
    const room = this.store.rooms().find((item) => item.number === row.room);
    if (room) this.store.finishStay(room.id);
    this.store.updateReservation(row.id, 'Finalizada');
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
    const row = this.selected();
    if (!row || this.companionForm.invalid) {
      this.error.set('Complete nombre y documento del acompañante.');
      return;
    }
    const value = this.companionForm.getRawValue();
    this.store.reservations.update((rows) =>
      rows.map((item) =>
        item.id === row.id
          ? { ...item, companion: undefined, companions: [...this.companions(item), value] }
          : item,
      ),
    );
    this.companionForm.reset();
    this.error.set('');
  }
  removeCompanion(index: number) {
    const row = this.selected();
    if (!row) return;
    this.store.reservations.update((rows) =>
      rows.map((item) =>
        item.id === row.id
          ? {
              ...item,
              companion: undefined,
              companions: this.companions(item).filter((_, i) => i !== index),
            }
          : item,
      ),
    );
  }
  openExtras(row: DemoReservation) {
    this.selectedId.set(row.id);
    this.extraForm.setValue({ itemId: 0, quantity: 1 });
    this.dialog.set('extras');
    this.error.set('');
  }
  addExtra() {
    const row = this.selected();
    const v = this.extraForm.getRawValue();
    const item = this.admin.catalog().find((entry) => entry.id === v.itemId);
    if (!row || !item || v.quantity < 1) {
      this.error.set('Seleccione un producto o servicio.');
      return;
    }
    const amount = item.price * v.quantity;
    this.store.reservations.update((rows) =>
      rows.map((entry) =>
        entry.id === row.id
          ? { ...entry, extras: [...entry.extras, item.name], total: entry.total + amount }
          : entry,
      ),
    );
    const id = Math.max(0, ...this.store.consumptions().map((entry) => entry.id)) + 1;
    this.store.consumptions.update((rows) => [
      {
        id,
        room: row.room,
        service: item.name,
        quantity: v.quantity,
        amount,
        time: new Intl.DateTimeFormat('es-PE', { hour: '2-digit', minute: '2-digit' }).format(
          new Date(),
        ),
      },
      ...rows,
    ]);
    this.admin.addLedger({
      description: `${item.name} · reserva ${row.code}`,
      kind: 'Consumo',
      amount,
      method: 'Efectivo',
      room: row.room,
      sourceId: id,
    });
    this.dialog.set(null);
  }
  askDelete(row: DemoReservation) {
    this.selectedId.set(row.id);
    this.dialog.set('delete');
  }
  deleteSelected() {
    const row = this.selected();
    if (!row) return;
    if (row.status === 'En curso') {
      const room = this.store.rooms().find((item) => item.number === row.room);
      if (room) this.store.finishStay(room.id);
    }
    this.store.reservations.update((rows) => rows.filter((item) => item.id !== row.id));
    this.dialog.set(null);
  }
  cancel(row: DemoReservation) {
    this.store.updateReservation(row.id, 'Cancelada');
  }
}
