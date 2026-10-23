import { isPlatformBrowser } from '@angular/common';
import { computed, effect, inject, Injectable, PLATFORM_ID, signal } from '@angular/core';
import { RoomRates } from './hotel-admin.store';

export type RoomStatus = 'Disponible' | 'Ocupada' | 'Limpieza' | 'Mantenimiento';
export const ROOM_RATES = { Estándar: 35, Superior: 40, Suite: 45 } as const;
export interface DemoStay {
  mode: 'Horas' | 'Noche';
  durationHours: number;
  startedAt: string;
  endsAt: string;
  paymentMethod: string;
}
export interface DemoRoom {
  id: number;
  number: string;
  floor: number;
  type: string;
  status: RoomStatus;
  rate: number;
  guest?: string;
  capacity?: number;
  bedType?: string;
  equipment?: string[];
  notes?: string;
  customRates?: RoomRates;
  stay?: DemoStay;
}
export interface DemoReservation {
  id: number;
  code: string;
  guest: string;
  room: string;
  from: string;
  to: string;
  status: 'Pendiente' | 'Confirmada' | 'En curso' | 'Finalizada' | 'Cancelada';
  extras: string[];
  total: number;
  companion?: { name: string; document: string; relationship: string };
  companions?: { name: string; document: string; relationship: string }[];
  mode?: 'Horas' | 'Noche';
  durationHours?: number;
  startAt?: string;
  endAt?: string;
}
export interface DemoGuest {
  id: number;
  name: string;
  document: string;
  phone: string;
  email: string;
  stays: number;
}
export interface DemoConsumption {
  id: number;
  room: string;
  service: string;
  quantity: number;
  amount: number;
  time: string;
  cancelled?: boolean;
}
export interface DemoEmployee {
  id: number;
  name: string;
  role: string;
  shift: string;
  status: 'Activo' | 'Descanso';
  document?: string;
  email?: string;
  phone?: string;
}
export interface DemoReceipt {
  id: number;
  type: string;
  number: string;
  guest: string;
  amount: number;
  date: string;
}
const seedStay = (
  mode: DemoStay['mode'],
  durationHours: number,
  minutesRemaining: number,
  paymentMethod: string,
): DemoStay => {
  const endsAt = new Date(Date.now() + minutesRemaining * 60_000);
  return {
    mode,
    durationHours,
    startedAt: new Date(endsAt.getTime() - durationHours * 3_600_000).toISOString(),
    endsAt: endsAt.toISOString(),
    paymentMethod,
  };
};

@Injectable({ providedIn: 'root' })
export class HotelDemoStore {
  private readonly browser = isPlatformBrowser(inject(PLATFORM_ID));
  readonly rooms = signal<DemoRoom[]>([
    {
      id: 1,
      number: '101',
      floor: 1,
      type: 'Estándar',
      status: 'Ocupada',
      rate: ROOM_RATES.Estándar,
      guest: 'Lucía Méndez',
      stay: seedStay('Noche', 24, 780, 'Efectivo'),
    },
    {
      id: 2,
      number: '102',
      floor: 1,
      type: 'Estándar',
      status: 'Disponible',
      rate: ROOM_RATES.Estándar,
    },
    {
      id: 3,
      number: '103',
      floor: 1,
      type: 'Matrimonial',
      status: 'Ocupada',
      rate: ROOM_RATES.Superior,
      guest: 'Carlos Benavides',
      stay: seedStay('Noche', 24, 600, 'Tarjeta'),
    },
    {
      id: 4,
      number: '104',
      floor: 1,
      type: 'Estándar',
      status: 'Limpieza',
      rate: ROOM_RATES.Estándar,
    },
    {
      id: 5,
      number: '201',
      floor: 2,
      type: 'Matrimonial',
      status: 'Disponible',
      rate: ROOM_RATES.Superior,
    },
    {
      id: 6,
      number: '202',
      floor: 2,
      type: 'Suite con Jacuzzi',
      status: 'Ocupada',
      rate: ROOM_RATES.Suite,
      guest: 'Sofía Rojas',
      stay: seedStay('Horas', 3, 15, 'Yape'),
    },
    {
      id: 7,
      number: '203',
      floor: 2,
      type: 'Matrimonial',
      status: 'Disponible',
      rate: ROOM_RATES.Superior,
    },
    {
      id: 8,
      number: '204',
      floor: 2,
      type: 'Estándar',
      status: 'Mantenimiento',
      rate: ROOM_RATES.Estándar,
    },
    {
      id: 9,
      number: '301',
      floor: 3,
      type: 'Suite con Jacuzzi',
      status: 'Disponible',
      rate: ROOM_RATES.Suite,
    },
    {
      id: 10,
      number: '302',
      floor: 3,
      type: 'Matrimonial',
      status: 'Limpieza',
      rate: ROOM_RATES.Superior,
    },
    {
      id: 11,
      number: '303',
      floor: 3,
      type: 'Estándar',
      status: 'Disponible',
      rate: ROOM_RATES.Estándar,
    },
    {
      id: 12,
      number: '304',
      floor: 3,
      type: 'Suite con Jacuzzi',
      status: 'Ocupada',
      rate: ROOM_RATES.Suite,
      guest: 'Martín Quiroz',
      stay: seedStay('Horas', 3, 45, 'Plin'),
    },
  ]);
  readonly reservations = signal<DemoReservation[]>([
    {
      id: 1,
      code: 'RS-2026-041',
      guest: 'Lucía Méndez',
      room: '101',
      from: '2026-10-21',
      to: '2026-10-24',
      status: 'En curso',
      extras: ['Desayuno'],
      total: 133,
    },
    {
      id: 2,
      code: 'RS-2026-042',
      guest: 'Carlos Benavides',
      room: '103',
      from: '2026-10-22',
      to: '2026-10-25',
      status: 'En curso',
      extras: ['Spa', 'Cochera'],
      total: 280,
    },
    {
      id: 3,
      code: 'RS-2026-043',
      guest: 'Sofía Rojas',
      room: '202',
      from: '2026-10-20',
      to: '2026-10-23',
      status: 'En curso',
      extras: ['Desayuno', 'Spa'],
      total: 293,
    },
    {
      id: 4,
      code: 'RS-2026-044',
      guest: 'Elena Vargas',
      room: '201',
      from: '2026-10-22',
      to: '2026-10-24',
      status: 'Confirmada',
      extras: ['Cochera'],
      total: 110,
    },
    {
      id: 5,
      code: 'RS-2026-045',
      guest: 'Diego Salazar',
      room: '301',
      from: '2026-10-23',
      to: '2026-10-25',
      status: 'Pendiente',
      extras: [],
      total: 90,
    },
    {
      id: 6,
      code: 'RS-2026-046',
      guest: 'Martín Quiroz',
      room: '304',
      from: '2026-10-21',
      to: '2026-10-23',
      status: 'En curso',
      extras: ['Desayuno'],
      total: 118,
    },
  ]);
  readonly guests = signal<DemoGuest[]>([
    {
      id: 1,
      name: 'Lucía Méndez',
      document: 'DNI 72491836',
      phone: '987 245 601',
      email: 'lucia.mendez@email.com',
      stays: 3,
    },
    {
      id: 2,
      name: 'Carlos Benavides',
      document: 'DNI 41862735',
      phone: '991 742 118',
      email: 'cbenavides@email.com',
      stays: 2,
    },
    {
      id: 3,
      name: 'Sofía Rojas',
      document: 'CE 00147529',
      phone: '945 308 660',
      email: 'sofia.rojas@email.com',
      stays: 1,
    },
    {
      id: 4,
      name: 'Elena Vargas',
      document: 'DNI 63251974',
      phone: '982 711 405',
      email: 'elena.vargas@email.com',
      stays: 1,
    },
    {
      id: 5,
      name: 'Diego Salazar',
      document: 'DNI 70518342',
      phone: '967 203 882',
      email: 'diego.salazar@email.com',
      stays: 0,
    },
    {
      id: 6,
      name: 'Martín Quiroz',
      document: 'DNI 44719328',
      phone: '978 415 239',
      email: 'martin.quiroz@email.com',
      stays: 2,
    },
  ]);
  readonly consumptions = signal<DemoConsumption[]>([
    { id: 1, room: '101', service: 'Desayuno buffet', quantity: 2, amount: 56, time: '08:15' },
    { id: 2, room: '202', service: 'Spa · masaje', quantity: 1, amount: 130, time: '10:30' },
    { id: 3, room: '103', service: 'Estacionamiento', quantity: 1, amount: 30, time: '11:05' },
  ]);
  readonly employees = signal<DemoEmployee[]>([
    { id: 1, name: 'Ana Torres', role: 'Recepción', shift: 'Mañana', status: 'Activo' },
    { id: 2, name: 'José Aguilar', role: 'Housekeeping', shift: 'Mañana', status: 'Activo' },
    { id: 3, name: 'María Paredes', role: 'Caja', shift: 'Mañana', status: 'Activo' },
    { id: 4, name: 'Pedro Flores', role: 'Mantenimiento', shift: 'Tarde', status: 'Descanso' },
    { id: 5, name: 'Rosa Castillo', role: 'Recepción', shift: 'Noche', status: 'Descanso' },
  ]);
  readonly cashOpen = signal(true);
  readonly cashInitial = signal(500);
  readonly receipts = signal<DemoReceipt[]>([
    {
      id: 1,
      type: 'Boleta',
      number: 'B001-000184',
      guest: 'Lucía Méndez',
      amount: 133,
      date: '22 oct, 09:15',
    },
    {
      id: 2,
      type: 'Factura',
      number: 'F001-000072',
      guest: 'Carlos Benavides',
      amount: 280,
      date: '22 oct, 10:42',
    },
  ]);
  readonly occupied = computed(() => this.rooms().filter((r) => r.status === 'Ocupada').length);
  readonly available = computed(() => this.rooms().filter((r) => r.status === 'Disponible').length);
  readonly cleaning = computed(() => this.rooms().filter((r) => r.status === 'Limpieza').length);
  readonly pendingCheckIns = computed(
    () =>
      this.reservations().filter((r) => r.status === 'Confirmada' && r.from === '2026-10-22')
        .length,
  );
  readonly cashIncome = computed(() => this.receipts().reduce((sum, r) => sum + r.amount, 0));
  constructor() {
    if (!this.browser) return;
    try {
      const saved = JSON.parse(
        localStorage.getItem('hotel-parda-operations-v1') || 'null',
      ) as Record<string, unknown> | null;
      if (saved) {
        if (Array.isArray(saved['rooms']))
          this.rooms.set(
            (saved['rooms'] as DemoRoom[]).map((room) => ({
              ...room,
              type:
                room.type === 'Superior'
                  ? 'Matrimonial'
                  : room.type === 'Suite'
                    ? 'Suite con Jacuzzi'
                    : room.type,
              stay:
                room.stay ||
                (room.status === 'Ocupada'
                  ? seedStay(
                      room.number === '202' || room.number === '304' ? 'Horas' : 'Noche',
                      room.number === '202' || room.number === '304' ? 3 : 24,
                      room.number === '202' ? 15 : 90,
                      'Efectivo',
                    )
                  : undefined),
            })),
          );
        if (Array.isArray(saved['reservations']))
          this.reservations.set(saved['reservations'] as DemoReservation[]);
        if (Array.isArray(saved['guests'])) this.guests.set(saved['guests'] as DemoGuest[]);
        if (Array.isArray(saved['consumptions']))
          this.consumptions.set(saved['consumptions'] as DemoConsumption[]);
        if (Array.isArray(saved['employees']))
          this.employees.set(saved['employees'] as DemoEmployee[]);
        if (Array.isArray(saved['receipts'])) this.receipts.set(saved['receipts'] as DemoReceipt[]);
        if (typeof saved['cashOpen'] === 'boolean') this.cashOpen.set(saved['cashOpen']);
        if (typeof saved['cashInitial'] === 'number') this.cashInitial.set(saved['cashInitial']);
      }
    } catch {
      /* Keep initial data if browser storage is invalid. */
    }
    effect(() => {
      localStorage.setItem(
        'hotel-parda-operations-v1',
        JSON.stringify({
          rooms: this.rooms(),
          reservations: this.reservations(),
          guests: this.guests(),
          consumptions: this.consumptions(),
          employees: this.employees(),
          receipts: this.receipts(),
          cashOpen: this.cashOpen(),
          cashInitial: this.cashInitial(),
        }),
      );
    });
  }
  updateRoom(id: number, status: RoomStatus, guest?: string) {
    this.rooms.update((rows) =>
      rows.map((r) =>
        r.id === id
          ? {
              ...r,
              status,
              guest: status === 'Ocupada' ? guest || r.guest : undefined,
              stay: status === 'Ocupada' ? r.stay : undefined,
            }
          : r,
      ),
    );
  }
  startStay(roomId: number, guest: string, stay: DemoStay) {
    this.rooms.update((rows) =>
      rows.map((room) => (room.id === roomId ? { ...room, status: 'Ocupada', guest, stay } : room)),
    );
  }
  extendStay(roomId: number, hours: number) {
    this.rooms.update((rows) =>
      rows.map((room) =>
        room.id === roomId && room.stay
          ? {
              ...room,
              stay: {
                ...room.stay,
                durationHours: room.stay.durationHours + hours,
                endsAt: new Date(Date.parse(room.stay.endsAt) + hours * 3_600_000).toISOString(),
              },
            }
          : room,
      ),
    );
  }
  finishStay(roomId: number) {
    const room = this.rooms().find((row) => row.id === roomId);
    if (!room) return;
    this.updateRoom(roomId, 'Limpieza');
    this.reservations.update((rows) =>
      rows.map((row) =>
        row.room === room.number && row.status === 'En curso'
          ? { ...row, status: 'Finalizada' }
          : row,
      ),
    );
  }
  moveStay(fromId: number, toId: number) {
    const source = this.rooms().find((row) => row.id === fromId);
    const target = this.rooms().find((row) => row.id === toId);
    if (!source || !target || source.status !== 'Ocupada' || target.status !== 'Disponible')
      return false;
    this.rooms.update((rows) =>
      rows.map((row) =>
        row.id === fromId
          ? { ...row, status: 'Limpieza', guest: undefined, stay: undefined }
          : row.id === toId
            ? { ...row, status: 'Ocupada', guest: source.guest, stay: source.stay }
            : row,
      ),
    );
    this.reservations.update((rows) =>
      rows.map((row) =>
        row.room === source.number && row.status === 'En curso'
          ? { ...row, room: target.number }
          : row,
      ),
    );
    return true;
  }
  updateReservation(id: number, status: DemoReservation['status']) {
    this.reservations.update((rows) => rows.map((r) => (r.id === id ? { ...r, status } : r)));
  }
}
