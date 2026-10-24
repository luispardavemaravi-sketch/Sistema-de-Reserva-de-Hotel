import { StatusPulseDirective } from '../../shared/motion/status-pulse.directive';
import { TiltDirective } from '../../shared/motion/tilt.directive';
import { isPlatformBrowser } from '@angular/common';
import { Component, computed, DestroyRef, inject, PLATFORM_ID, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { HotelAdminStore, CatalogItem } from '../../core/demo/hotel-admin.store';
import { DemoRoom, HotelDemoStore, RoomStatus } from '../../core/demo/hotel-demo.store';
import { ModalFocusDirective } from '../../shared/components/modal-focus.directive';

@Component({
  selector: 'app-recepcion',
  imports: [StatusPulseDirective, TiltDirective, ReactiveFormsModule, ModalFocusDirective],
  templateUrl: './recepcion.component.html',
  styles: [
    `
      .legend {
        display: flex;
        gap: 17px;
        flex-wrap: wrap;
        font-size: 12px;
        color: var(--muted, #586b74);
      }
      .legend i {
        width: 8px;
        height: 8px;
        border-radius: 50%;
        display: inline-block;
        margin-right: 5px;
      }
      .room-grid {
        display: grid;
        grid-template-columns: repeat(4, minmax(0, 1fr));
        gap: 12px;
      }
      .floor {
        margin: 23px 0;
      }
      .floor h2 {
        font-size: 16px;
        margin: 0 0 12px;
      }
      .floor h2 span {
        font-size: 12px;
        font-weight: 400;
        color: var(--muted, #60717c);
        margin-left: 8px;
      }
      .room-card {
        background: var(--surface, #fff);
        border: 1px solid var(--line, #e1e8eb);
        border-left: 4px solid #aeb8bf;
        border-radius: 9px;
        padding: 12px;
        min-width: 0;
      }
      .room-card.status-disponible {
        border-left-color: #5c9d51;
      }
      .room-card.status-ocupada {
        border-left-color: #a23838;
      }
      .room-card.status-limpieza {
        border-left-color: #d8a34c;
      }
      .room-open {
        display: grid;
        text-align: left;
        background: none;
        border: 0;
        color: inherit;
        width: 100%;
        padding: 2px;
        gap: 6px;
      }
      .room-top {
        display: flex;
        justify-content: space-between;
      }
      .room-top strong {
        font-size: 23px;
        font-variant-numeric: tabular-nums;
      }
      .state-dot {
        width: 9px;
        height: 9px;
        border-radius: 50%;
        background: #aeb8bf;
      }
      .status-disponible .state-dot {
        background: #5c9d51;
      }
      .status-ocupada .state-dot {
        background: #a23838;
      }
      .status-limpieza .state-dot {
        background: #d8a34c;
      }
      .room-type,
      .room-detail {
        font-size: 13px;
        color: var(--muted, #5b6c78);
      }
      .room-status {
        font-weight: 600;
        font-size: 13px;
      }
      .quick-state {
        width: 100%;
        margin-top: 10px;
        font-size: 12px;
        min-height: 36px;
      }
      .control-dialog {
        max-width: 690px;
      }
      .operation-summary {
        display: grid;
        grid-template-columns: repeat(3, 1fr);
        gap: 10px;
        margin-bottom: 17px;
      }
      .operation-summary div {
        padding: 12px;
        border: 1px solid var(--line, #e3e9ec);
        border-radius: 7px;
      }
      .operation-summary small {
        display: block;
        color: var(--muted, #65747e);
        font-size: 11px;
      }
      .operation-summary strong {
        display: block;
        margin-top: 5px;
      }
      .subhead {
        font-size: 14px;
        font-weight: 600;
        margin: 18px 0 9px;
      }
      .check-row {
        display: flex;
        gap: 12px;
        flex-wrap: wrap;
        font-size: 13px;
      }
      .total-row {
        display: flex;
        justify-content: space-between;
        align-items: center;
        border-top: 1px solid var(--line, #e1e8eb);
        padding-top: 15px;
        margin-top: 18px;
      }
      .total-row strong {
        font-size: 22px;
      }
      .quick-actions {
        display: flex;
        gap: 8px;
        flex-wrap: wrap;
      }
      .error {
        color: #b43a3a;
        font-size: 13px;
      }
      .divider {
        border-top: 1px solid var(--line, #e1e8eb);
        margin: 18px 0;
      }
      .inline-action {
        background: var(--raised, #f5f8f9);
        border-radius: 8px;
        padding: 12px;
        margin-top: 12px;
      }
      .inline-action .form-grid {
        align-items: end;
      }
      .expiry {
        color: #a23838;
        font-weight: 600;
      }
      @media (max-width: 1000px) {
        .room-grid {
          grid-template-columns: repeat(3, 1fr);
        }
      }
      @media (max-width: 700px) {
        .room-grid {
          grid-template-columns: repeat(2, 1fr);
        }
        .operation-summary {
          grid-template-columns: 1fr 1fr;
        }
      }
      @media (max-width: 390px) {
        .room-grid {
          grid-template-columns: 1fr 1fr;
          gap: 7px;
        }
        .room-card {
          padding: 8px;
        }
        .control-dialog {
          padding: 15px;
        }
        .operation-summary {
          grid-template-columns: 1fr;
        }
      }
    `,
  ],
})
export class RecepcionComponent {
  readonly store = inject(HotelDemoStore);
  readonly admin = inject(HotelAdminStore);
  private readonly fb = inject(FormBuilder);
  private readonly destroyRef = inject(DestroyRef);
  readonly now = signal(Date.now());
  readonly filter = signal('Todos');
  readonly selectedId = signal<number | null>(null);
  readonly selected = computed(
    () => this.store.rooms().find((room) => room.id === this.selectedId()) || null,
  );
  readonly visibleRooms = computed(() =>
    this.store.rooms().filter((room) => this.filter() === 'Todos' || room.status === this.filter()),
  );
  readonly activeAction = signal<'consumo' | 'mover' | null>(null);
  readonly error = signal('');
  readonly notice = signal('');
  readonly walkInForm = this.fb.nonNullable.group({
    mode: ['Horas'],
    durationHours: [3],
    guest: [''],
    document: [''],
    anonymous: [false],
    companionName: [''],
    companionDocument: [''],
    relationship: ['Familiar'],
    breakfast: [false],
    parking: [false],
    spa: [false],
    productId: [0],
    productQuantity: [1, Validators.min(1)],
    method: ['Efectivo', Validators.required],
  });
  readonly consumptionForm = this.fb.nonNullable.group({
    itemId: [0, Validators.required],
    quantity: [1, [Validators.required, Validators.min(1)]],
  });
  readonly moveForm = this.fb.nonNullable.group({ roomId: [0, Validators.required] });
  constructor() {
    if (isPlatformBrowser(inject(PLATFORM_ID))) {
      const timer = setInterval(() => this.now.set(Date.now()), 30_000);
      this.destroyRef.onDestroy(() => clearInterval(timer));
    }
  }
  setFilter(event: Event) {
    this.filter.set((event.target as HTMLSelectElement).value);
  }
  countOnFloor(floor: number) {
    return this.visibleRooms().filter((room) => room.floor === floor).length;
  }
  open(room: DemoRoom) {
    this.selectedId.set(room.id);
    this.activeAction.set(null);
    this.error.set('');
    this.walkInForm.reset();
    this.walkInForm.patchValue({
      mode: 'Horas',
      durationHours: 3,
      method: this.admin.paymentMethods()[0] || 'Efectivo',
    });
  }
  close() {
    this.selectedId.set(null);
    this.activeAction.set(null);
    this.error.set('');
  }
  quickStatus(room: DemoRoom, event: Event) {
    const status = (event.target as HTMLSelectElement).value as RoomStatus;
    if (status === 'Ocupada' && room.status !== 'Ocupada') {
      (event.target as HTMLSelectElement).value = room.status;
      if (room.status !== 'Disponible') this.store.updateRoom(room.id, 'Disponible');
      this.open(room);
      return;
    }
    if (room.status === 'Ocupada' && status !== 'Ocupada') this.store.finishStay(room.id);
    if (status !== 'Ocupada') this.store.updateRoom(room.id, status);
  }
  rates(room: DemoRoom) {
    return this.admin.ratesFor(room);
  }
  stayLabel(room: DemoRoom) {
    if (!room.stay) return room.status === 'Ocupada' ? 'En curso' : 'Sin estancia';
    return room.stay.mode === 'Horas'
      ? `Por ${room.stay.durationHours} horas · Sale ${this.time(room.stay.endsAt)}`
      : `Por noche · Sale ${this.time(room.stay.endsAt)}`;
  }
  time(iso: string) {
    return new Intl.DateTimeFormat('es-PE', {
      hour: '2-digit',
      minute: '2-digit',
      hour12: false,
    }).format(new Date(iso));
  }
  dateTime(iso: string) {
    return new Intl.DateTimeFormat('es-PE', { dateStyle: 'short', timeStyle: 'short' }).format(
      new Date(iso),
    );
  }
  remaining(room: DemoRoom) {
    if (!room.stay) return 'Sin hora registrada';
    const minutes = Math.ceil((Date.parse(room.stay.endsAt) - this.now()) / 60_000);
    return minutes > 0 ? `${minutes} min restantes` : `${Math.abs(minutes)} min vencidos`;
  }
  roomConsumptions(room: DemoRoom) {
    return this.store.consumptions().filter((row) => row.room === room.number && !row.cancelled);
  }
  consumptionTotal(room: DemoRoom) {
    return this.roomConsumptions(room).reduce((sum, row) => sum + row.amount, 0);
  }
  availableRooms(room: DemoRoom) {
    return this.store.rooms().filter((row) => row.status === 'Disponible' && row.id !== room.id);
  }
  catalogItem(id: number): CatalogItem | undefined {
    return this.admin.catalog().find((item) => item.id === id);
  }
  basePrice(room: DemoRoom) {
    const value = this.walkInForm.getRawValue();
    const rates = this.rates(room);
    if (value.mode === 'Noche') return value.durationHours === 12 ? rates.night12 : rates.night24;
    return value.durationHours === 2
      ? rates.hours2
      : value.durationHours === 4
        ? rates.hours4
        : value.durationHours === 6
          ? rates.hours6
          : rates.hours3;
  }
  walkInTotal(room: DemoRoom) {
    const value = this.walkInForm.getRawValue();
    const item = this.catalogItem(value.productId);
    return (
      this.basePrice(room) +
      (value.breakfast ? 28 : 0) +
      (value.parking ? 30 : 0) +
      (value.spa ? 130 : 0) +
      (item ? item.price * value.productQuantity : 0)
    );
  }
  saveWalkIn() {
    const room = this.selected();
    if (!room || room.status !== 'Disponible') return;
    const v = this.walkInForm.getRawValue();
    if (!v.anonymous && !v.guest.trim() && !v.document.trim()) {
      this.error.set('Escriba el nombre o documento, o marque ingreso rápido.');
      return;
    }
    if ((v.companionName || v.companionDocument) && !(v.companionName && v.companionDocument)) {
      this.error.set('Complete nombre y documento del acompañante.');
      return;
    }
    const guest = v.anonymous ? 'Ingreso rápido' : v.guest.trim() || `DNI ${v.document.trim()}`;
    const startedAt = new Date();
    const endsAt = new Date(startedAt.getTime() + v.durationHours * 3_600_000);
    this.store.startStay(room.id, guest, {
      mode: v.mode as 'Horas' | 'Noche',
      durationHours: v.durationHours,
      startedAt: startedAt.toISOString(),
      endsAt: endsAt.toISOString(),
      paymentMethod: v.method,
    });
    const base = this.basePrice(room);
    this.admin.addLedger({
      description: `Habitación ${room.number} · ${v.durationHours} horas`,
      kind: 'Alojamiento',
      amount: base,
      method: v.method,
      room: room.number,
      roomType: room.type,
      stayMode: v.mode as 'Horas' | 'Noche',
    });
    const extras = [
      v.breakfast && this.admin.catalog().find((item) => item.name === 'Desayuno buffet'),
      v.parking && this.admin.catalog().find((item) => item.name === 'Estacionamiento'),
      v.spa && this.admin.catalog().find((item) => item.name === 'Spa · masaje'),
      this.catalogItem(v.productId),
    ].filter((item): item is CatalogItem => !!item);
    for (const item of extras) {
      const quantity = item.id === v.productId ? v.productQuantity : 1;
      this.addConsumption(room, item, quantity, v.method);
    }
    const id = Math.max(0, ...this.store.reservations().map((row) => row.id)) + 1;
    this.store.reservations.update((rows) => [
      {
        id,
        code: `RS-${new Date().getFullYear()}-${String(id + 40).padStart(3, '0')}`,
        guest,
        room: room.number,
        from: startedAt.toISOString().slice(0, 10),
        to: endsAt.toISOString().slice(0, 10),
        status: 'En curso',
        mode: v.mode as 'Horas' | 'Noche',
        durationHours: v.durationHours,
        extras: extras.map((item) => item.name),
        total: this.walkInTotal(room),
        companion: v.companionName
          ? { name: v.companionName, document: v.companionDocument, relationship: v.relationship }
          : undefined,
      },
      ...rows,
    ]);
    this.close();
    this.notice.set('Cambio aplicado en demostración. No se guardó en PostgreSQL.');
  }
  addConsumption(room: DemoRoom, item: CatalogItem, quantity: number, method: string) {
    const id = Math.max(0, ...this.store.consumptions().map((row) => row.id)) + 1;
    const amount = item.price * quantity;
    this.store.consumptions.update((rows) => [
      {
        id,
        room: room.number,
        service: item.name,
        quantity,
        amount,
        time: this.time(new Date().toISOString()),
      },
      ...rows,
    ]);
    this.admin.addLedger({
      description: `${item.name} · habitación ${room.number}`,
      kind: 'Consumo',
      amount,
      method,
      room: room.number,
      sourceId: id,
    });
  }
  saveConsumption() {
    const room = this.selected();
    const value = this.consumptionForm.getRawValue();
    const item = this.catalogItem(value.itemId);
    if (!room || !item) {
      this.error.set('Seleccione un producto o servicio.');
      return;
    }
    this.addConsumption(room, item, value.quantity, room.stay?.paymentMethod || 'Efectivo');
    this.activeAction.set(null);
    this.consumptionForm.reset();
  }
  extend(hours: number) {
    const room = this.selected();
    if (!room || !room.stay) return;
    this.store.extendStay(room.id, hours);
    const price = hours === 1 ? this.rates(room).extraHour : this.rates(room).night24;
    this.admin.addLedger({
      description: `Extensión habitación ${room.number} · ${hours}h`,
      kind: 'Alojamiento',
      amount: price,
      method: room.stay.paymentMethod,
      room: room.number,
      roomType: room.type,
      stayMode: room.stay.mode,
    });
  }
  move() {
    const room = this.selected();
    if (!room) return;
    if (!this.store.moveStay(room.id, this.moveForm.getRawValue().roomId)) {
      this.error.set('Seleccione una habitación disponible.');
      return;
    }
    this.close();
  }
  finish() {
    const room = this.selected();
    if (!room) return;
    this.store.finishStay(room.id);
    this.close();
  }
}
