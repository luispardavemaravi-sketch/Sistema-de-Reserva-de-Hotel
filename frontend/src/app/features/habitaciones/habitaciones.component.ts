import { AnimatedNumberComponent } from '../../shared/motion/animated-number.component';
import { TiltDirective } from '../../shared/motion/tilt.directive';
import { Component, computed, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { HotelAdminStore, RoomRates, RoomTypeConfig } from '../../core/demo/hotel-admin.store';
import { DemoRoom, HotelDemoStore, RoomStatus } from '../../core/demo/hotel-demo.store';
import { ModalFocusDirective } from '../../shared/components/modal-focus.directive';

type Tab = 'Habitaciones' | 'Pisos' | 'Tipos' | 'Precios' | 'Equipamiento';
type Dialog = 'room' | 'floor' | 'type' | 'equipment' | 'status' | null;
type DeleteTarget = {
  kind: 'room' | 'floor' | 'type' | 'equipment';
  id: number | string;
  label: string;
};

@Component({
  selector: 'app-habitaciones',
  imports: [AnimatedNumberComponent, TiltDirective, ReactiveFormsModule, ModalFocusDirective],
  templateUrl: './habitaciones.component.html',
  styles: [
    `
      .tab-panel {
        margin-top: 20px;
      }
      .room-table {
        min-width: 1120px;
      }
      .room-table td {
        vertical-align: top;
      }
      .room-table .notes {
        max-width: 180px;
        color: var(--muted, #5c6d78);
      }
      .rate-line {
        white-space: nowrap;
        font-size: 12px;
        line-height: 1.7;
      }
      .rate-line strong {
        font-weight: 600;
      }
      .actions {
        display: flex;
        gap: 5px;
        flex-wrap: wrap;
      }
      .actions .btn {
        min-height: 29px;
        padding: 4px 7px;
        font-size: 11px;
      }
      .config-grid {
        display: grid;
        grid-template-columns: repeat(3, 1fr);
        gap: 14px;
      }
      .config-card h3 {
        margin: 0 0 10px;
        font-size: 15px;
      }
      .config-card .actions {
        margin-top: 13px;
      }
      .large-dialog {
        max-width: 800px;
      }
      .modal-section {
        border-top: 1px solid var(--line, #e5ebee);
        padding-top: 16px;
        margin-top: 18px;
      }
      .modal-section h3 {
        font-size: 15px;
        margin: 0 0 12px;
      }
      .rate-grid {
        display: grid;
        grid-template-columns: repeat(4, 1fr);
        gap: 10px;
      }
      .check-grid {
        display: grid;
        grid-template-columns: repeat(3, 1fr);
        gap: 9px;
      }
      .check-grid label {
        font-size: 13px;
      }
      .inline-create {
        display: flex;
        gap: 7px;
        align-items: end;
      }
      .inline-create .field {
        min-width: 0;
        width: 100%;
      }
      .inline-create .btn {
        white-space: nowrap;
      }
      .inline-panel {
        background: var(--raised, #f5f8f9);
        border: 1px solid var(--line, #e0e8ec);
        border-radius: 7px;
        padding: 12px;
        margin-top: 8px;
      }
      .full {
        grid-column: 1/-1;
      }
      .notice {
        color: #ab3434;
        font-size: 13px;
      }
      .filter-row {
        display: flex;
        justify-content: space-between;
        gap: 10px;
        margin-bottom: 13px;
      }
      @media (max-width: 800px) {
        .config-grid {
          grid-template-columns: repeat(2, 1fr);
        }
        .rate-grid {
          grid-template-columns: repeat(2, 1fr);
        }
      }
      @media (max-width: 600px) {
        .config-grid,
        .check-grid {
          grid-template-columns: 1fr;
        }
        .large-dialog {
          padding: 16px;
        }
        .rate-grid {
          grid-template-columns: repeat(2, 1fr);
        }
      }
    `,
  ],
})
export class HabitacionesComponent {
  readonly store = inject(HotelDemoStore);
  readonly admin = inject(HotelAdminStore);
  private readonly fb = inject(FormBuilder);
  readonly tabs: Tab[] = ['Habitaciones', 'Pisos', 'Tipos', 'Precios', 'Equipamiento'];
  readonly activeTab = signal<Tab>('Habitaciones');
  readonly dialog = signal<Dialog>(null);
  readonly editingRoom = signal<number | null>(null);
  readonly editingConfig = signal<number | string | null>(null);
  readonly deleteTarget = signal<DeleteTarget | null>(null);
  readonly statusRoom = signal<DemoRoom | null>(null);
  readonly selectedEquipment = signal<string[]>([]);
  readonly inline = signal<'floor' | 'type' | 'equipment' | null>(null);
  readonly error = signal('');
  readonly notice = signal('');
  readonly query = signal('');
  readonly visibleRooms = computed(() =>
    this.store
      .rooms()
      .filter((room) =>
        `${room.number} ${room.type} ${room.floor}`
          .toLowerCase()
          .includes(this.query().toLowerCase()),
      ),
  );
  readonly roomForm = this.fb.nonNullable.group({
    number: ['', Validators.required],
    floor: [1, Validators.required],
    type: ['', Validators.required],
    inherit: [true],
    hours2: [20, Validators.min(0)],
    hours3: [25, Validators.min(0)],
    hours4: [30, Validators.min(0)],
    hours6: [40, Validators.min(0)],
    night12: [30, Validators.min(0)],
    night24: [35, Validators.min(0)],
    extraHour: [10, Validators.min(0)],
    capacity: [2, [Validators.required, Validators.min(1)]],
    bedType: ['2 plazas'],
    notes: [''],
  });
  readonly floorForm = this.fb.nonNullable.group({
    number: [1, [Validators.required, Validators.min(1), Validators.max(20)]],
    name: ['', Validators.required],
  });
  readonly typeForm = this.fb.nonNullable.group({
    name: ['', Validators.required],
    capacity: [2, [Validators.required, Validators.min(1)]],
    costPerStay: [0, Validators.min(0)],
    hours2: [20],
    hours3: [25],
    hours4: [30],
    hours6: [40],
    night12: [30],
    night24: [35],
    extraHour: [10],
  });
  readonly equipmentForm = this.fb.nonNullable.group({ name: ['', Validators.required] });
  readonly statusForm = this.fb.nonNullable.group({ status: ['Disponible'] });
  readonly inlineForm = this.fb.nonNullable.group({
    name: ['', Validators.required],
    number: [1],
    capacity: [2],
  });

  setQuery(event: Event) {
    this.query.set((event.target as HTMLInputElement).value);
  }
  rates(room: DemoRoom) {
    return this.admin.ratesFor(room);
  }
  roomCountForFloor(floor: number) {
    return this.store.rooms().filter((room) => room.floor === floor).length;
  }
  equipmentCount(name: string) {
    return this.store.rooms().filter((room) => room.equipment?.includes(name)).length;
  }
  statusClass(status: RoomStatus) {
    return `badge ${status === 'Disponible' ? 'badge-room-available' : status === 'Ocupada' ? 'badge-room-occupied' : status === 'Limpieza' ? 'badge-amber' : 'badge-gray'}`;
  }
  openCreateRoom() {
    this.editingRoom.set(null);
    this.error.set('');
    this.selectedEquipment.set([]);
    this.inline.set(null);
    this.roomForm.reset();
    this.roomForm.patchValue({
      floor: this.admin.floors()[0]?.number || 1,
      type: this.admin.roomTypes()[0]?.name || '',
      inherit: true,
    });
    this.setRateFields(this.admin.roomTypes()[0]?.rates);
    this.dialog.set('room');
  }
  openEditRoom(room: DemoRoom) {
    this.editingRoom.set(room.id);
    this.error.set('');
    this.inline.set(null);
    this.selectedEquipment.set(room.equipment || []);
    this.roomForm.patchValue({
      number: room.number,
      floor: room.floor,
      type: room.type,
      inherit: !room.customRates,
      capacity: room.capacity || 2,
      bedType: room.bedType || '2 plazas',
      notes: room.notes || '',
    });
    this.setRateFields(this.rates(room));
    this.dialog.set('room');
  }
  duplicateRoom(room: DemoRoom) {
    const id = Math.max(0, ...this.store.rooms().map((row) => row.id)) + 1;
    this.store.rooms.update((rows) => [
      ...rows,
      {
        ...room,
        id,
        number: `${room.number}-Copia`,
        status: 'Disponible',
        guest: undefined,
        stay: undefined,
      },
    ]);
  }
  openStatus(room: DemoRoom) {
    this.statusRoom.set(room);
    this.statusForm.setValue({ status: room.status });
    this.dialog.set('status');
  }
  saveStatus() {
    const room = this.statusRoom();
    if (room) {
      this.store.updateRoom(room.id, this.statusForm.getRawValue().status as RoomStatus);
    }
    this.dialog.set(null);
    this.notice.set('Cambio aplicado en demostración. No se guardó en PostgreSQL.');
  }
  setRateFields(rates?: RoomRates) {
    if (rates) this.roomForm.patchValue(rates);
  }
  onTypeChanged(event: Event) {
    const name = (event.target as HTMLSelectElement).value;
    const type = this.admin.roomTypes().find((item) => item.name === name);
    if (type && this.roomForm.controls.inherit.value) {
      this.setRateFields(type.rates);
      this.roomForm.controls.capacity.setValue(type.capacity);
    }
  }
  onInheritChanged() {
    if (this.roomForm.controls.inherit.value) {
      const type = this.admin
        .roomTypes()
        .find((item) => item.name === this.roomForm.controls.type.value);
      this.setRateFields(type?.rates);
    }
  }
  toggleEquipment(name: string) {
    this.selectedEquipment.update((items) =>
      items.includes(name) ? items.filter((item) => item !== name) : [...items, name],
    );
  }
  saveRoom() {
    if (this.roomForm.invalid) {
      this.error.set('Complete los datos obligatorios y revise las tarifas.');
      return;
    }
    const v = this.roomForm.getRawValue();
    const id = this.editingRoom();
    if (
      this.store
        .rooms()
        .some(
          (room) => room.number.toLowerCase() === v.number.trim().toLowerCase() && room.id !== id,
        )
    ) {
      this.error.set('Ya existe una habitación con ese número o nombre.');
      return;
    }
    const rates: RoomRates = {
      hours2: v.hours2,
      hours3: v.hours3,
      hours4: v.hours4,
      hours6: v.hours6,
      night12: v.night12,
      night24: v.night24,
      extraHour: v.extraHour,
    };
    const current = this.store.rooms().find((room) => room.id === id);
    const room: DemoRoom = {
      id: id ?? Math.max(0, ...this.store.rooms().map((row) => row.id)) + 1,
      number: v.number.trim(),
      floor: v.floor,
      type: v.type,
      status: current?.status || 'Disponible',
      guest: current?.guest,
      stay: current?.stay,
      rate: v.night24,
      capacity: v.capacity,
      bedType: v.bedType,
      equipment: this.selectedEquipment(),
      notes: v.notes,
      customRates: v.inherit ? undefined : rates,
    };
    this.store.rooms.update((rows) =>
      id === null ? [...rows, room] : rows.map((item) => (item.id === id ? room : item)),
    );
    this.dialog.set(null);
    this.notice.set('Cambio aplicado en demostración. No se guardó en PostgreSQL.');
  }
  openConfig(kind: 'floor' | 'type' | 'equipment', id?: number | string) {
    this.error.set('');
    this.editingConfig.set(id ?? null);
    this.dialog.set(kind);
    if (kind === 'floor') {
      const item = this.admin.floors().find((row) => row.id === id);
      this.floorForm.setValue({
        number: item?.number || Math.max(0, ...this.admin.floors().map((row) => row.number)) + 1,
        name: item?.name || '',
      });
    }
    if (kind === 'type') {
      const item = this.admin.roomTypes().find((row) => row.id === id);
      this.typeForm.setValue({
        name: item?.name || '',
        capacity: item?.capacity || 2,
        costPerStay: item?.costPerStay || 0,
        ...(item?.rates ||
          this.admin.roomTypes()[0]?.rates || {
            hours2: 20,
            hours3: 25,
            hours4: 30,
            hours6: 40,
            night12: 30,
            night24: 35,
            extraHour: 10,
          }),
      });
    }
    if (kind === 'equipment') {
      this.equipmentForm.setValue({ name: typeof id === 'string' ? id : '' });
    }
  }
  saveConfig() {
    const kind = this.dialog();
    const id = this.editingConfig();
    if (kind === 'floor') {
      if (this.floorForm.invalid) {
        this.error.set('Ingrese piso y nombre válidos.');
        return;
      }
      const v = this.floorForm.getRawValue();
      if (this.admin.floors().some((row) => row.number === v.number && row.id !== id)) {
        this.error.set('Ese número de piso ya existe.');
        return;
      }
      this.admin.floors.update((rows) =>
        id === null
          ? [...rows, { id: Math.max(0, ...rows.map((row) => row.id)) + 1, ...v }]
          : rows.map((row) => (row.id === id ? { ...row, ...v } : row)),
      );
    }
    if (kind === 'type') {
      if (this.typeForm.invalid) {
        this.error.set('Complete el tipo de habitación.');
        return;
      }
      const v = this.typeForm.getRawValue();
      if (
        this.admin
          .roomTypes()
          .some((row) => row.name.toLowerCase() === v.name.toLowerCase() && row.id !== id)
      ) {
        this.error.set('Ese tipo ya existe.');
        return;
      }
      const rates: RoomRates = {
        hours2: v.hours2,
        hours3: v.hours3,
        hours4: v.hours4,
        hours6: v.hours6,
        night12: v.night12,
        night24: v.night24,
        extraHour: v.extraHour,
      };
      const item: RoomTypeConfig = {
        id:
          typeof id === 'number'
            ? id
            : Math.max(0, ...this.admin.roomTypes().map((row) => row.id)) + 1,
        name: v.name.trim(),
        capacity: v.capacity,
        costPerStay: v.costPerStay,
        rates,
      };
      const old = this.admin.roomTypes().find((row) => row.id === id);
      this.admin.roomTypes.update((rows) =>
        id === null ? [...rows, item] : rows.map((row) => (row.id === id ? item : row)),
      );
      if (old && old.name !== item.name)
        this.store.rooms.update((rows) =>
          rows.map((row) => (row.type === old.name ? { ...row, type: item.name } : row)),
        );
    }
    if (kind === 'equipment') {
      if (this.equipmentForm.invalid) {
        this.error.set('Ingrese el nombre del equipamiento.');
        return;
      }
      const value = this.equipmentForm.getRawValue().name.trim();
      if (
        this.admin
          .equipment()
          .some((name) => name.toLowerCase() === value.toLowerCase() && name !== id)
      ) {
        this.error.set('Ese equipamiento ya existe.');
        return;
      }
      this.admin.equipment.update((items) =>
        id === null ? [...items, value] : items.map((item) => (item === id ? value : item)),
      );
      if (typeof id === 'string' && id !== value)
        this.store.rooms.update((rows) =>
          rows.map((row) => ({
            ...row,
            equipment: row.equipment?.map((item) => (item === id ? value : item)),
          })),
        );
    }
    this.dialog.set(null);
    this.notice.set('Cambio aplicado en demostración. No se guardó en PostgreSQL.');
  }
  saveInline() {
    if (this.inlineForm.invalid) {
      this.error.set('Ingrese un nombre.');
      return;
    }
    const v = this.inlineForm.getRawValue();
    if (this.inline() === 'floor') {
      if (this.admin.floors().some((row) => row.number === v.number)) {
        this.error.set('Ese piso ya existe.');
        return;
      }
      this.admin.floors.update((rows) => [
        ...rows,
        { id: Math.max(0, ...rows.map((row) => row.id)) + 1, number: v.number, name: v.name },
      ]);
      this.roomForm.controls.floor.setValue(v.number);
    }
    if (this.inline() === 'type') {
      const rates = this.admin.roomTypes()[0]?.rates;
      if (!rates) return;
      this.admin.roomTypes.update((rows) => [
        ...rows,
        {
          id: Math.max(0, ...rows.map((row) => row.id)) + 1,
          name: v.name,
          capacity: v.capacity,
          costPerStay: 0,
          rates: { ...rates },
        },
      ]);
      this.roomForm.controls.type.setValue(v.name);
      this.setRateFields(rates);
    }
    if (this.inline() === 'equipment') {
      this.admin.equipment.update((rows) => [...rows, v.name]);
      this.selectedEquipment.update((rows) => [...rows, v.name]);
    }
    this.inline.set(null);
    this.inlineForm.reset();
    this.error.set('');
  }
  askDelete(kind: DeleteTarget['kind'], id: number | string, label: string) {
    this.deleteTarget.set({ kind, id, label });
  }
  deleteSelected() {
    const target = this.deleteTarget();
    if (!target) return;
    const { kind, id } = target;
    if (kind === 'room') {
      const room = this.store.rooms().find((row) => row.id === id);
      if (room?.status === 'Ocupada') {
        this.error.set('No se puede eliminar una habitación ocupada.');
        this.deleteTarget.set(null);
        return;
      }
      this.store.rooms.update((rows) => rows.filter((row) => row.id !== id));
    }
    if (kind === 'floor') {
      const floor = this.admin.floors().find((row) => row.id === id);
      if (floor && this.store.rooms().some((row) => row.floor === floor.number)) {
        this.error.set('Este piso tiene habitaciones asignadas.');
        this.deleteTarget.set(null);
        return;
      }
      this.admin.floors.update((rows) => rows.filter((row) => row.id !== id));
    }
    if (kind === 'type') {
      const type = this.admin.roomTypes().find((row) => row.id === id);
      if (type && this.store.rooms().some((row) => row.type === type.name)) {
        this.error.set('Este tipo tiene habitaciones asignadas.');
        this.deleteTarget.set(null);
        return;
      }
      this.admin.roomTypes.update((rows) => rows.filter((row) => row.id !== id));
    }
    if (kind === 'equipment') {
      this.admin.equipment.update((rows) => rows.filter((row) => row !== id));
      this.store.rooms.update((rows) =>
        rows.map((row) => ({ ...row, equipment: row.equipment?.filter((item) => item !== id) })),
      );
    }
    this.deleteTarget.set(null);
  }
}
