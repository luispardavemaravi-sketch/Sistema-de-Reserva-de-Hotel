import { computed, Injectable, signal } from '@angular/core';
import type { DemoRoom } from './hotel-demo.store';

export interface RoomRates {
  hours2: number;
  hours3: number;
  hours4: number;
  hours6: number;
  night12: number;
  night24: number;
  extraHour: number;
}
export interface RoomTypeConfig {
  id: number;
  name: string;
  capacity: number;
  costPerStay: number;
  rates: RoomRates;
}
export interface HotelProfile {
  name: string;
  ruc: string;
  currency: string;
}
export interface HotelFloor {
  id: number;
  number: number;
  name: string;
}
export interface CatalogItem {
  id: number;
  name: string;
  category: string;
  area: string;
  price: number;
  active: boolean;
}
export interface ShiftConfig {
  id: number;
  name: string;
  start: string;
  end: string;
}
export interface Promotion {
  id: number;
  name: string;
  discount: number;
  active: boolean;
}
export type PaymentMethod = 'Efectivo' | 'Yape' | 'Plin' | 'Tarjeta' | 'Transferencia' | string;
export type LedgerKind = 'Alojamiento' | 'Consumo' | 'Ingreso' | 'Gasto';
export interface LedgerEntry {
  id: number;
  date: string;
  hour?: number;
  description: string;
  kind: LedgerKind;
  amount: number;
  method: PaymentMethod;
  room?: string;
  roomType?: string;
  stayMode?: 'Horas' | 'Noche';
  sourceId?: number;
}

const day = (offset: number) => {
  const date = new Date();
  date.setHours(12, 0, 0, 0);
  date.setDate(date.getDate() + offset);
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
};

@Injectable({ providedIn: 'root' })
export class HotelAdminStore {
  readonly profile = signal<HotelProfile>({ name: 'Hotel Parda', ruc: '', currency: 'PEN' });
  readonly floors = signal<HotelFloor[]>([
    { id: 1, number: 1, name: 'Piso 1' },
    { id: 2, number: 2, name: 'Piso 2' },
    { id: 3, number: 3, name: 'Piso 3' },
  ]);
  readonly roomTypes = signal<RoomTypeConfig[]>([
    {
      id: 1,
      name: 'Estándar',
      capacity: 2,
      costPerStay: 8,
      rates: {
        hours2: 20,
        hours3: 25,
        hours4: 30,
        hours6: 40,
        night12: 30,
        night24: 35,
        extraHour: 10,
      },
    },
    {
      id: 2,
      name: 'Matrimonial',
      capacity: 2,
      costPerStay: 10,
      rates: {
        hours2: 25,
        hours3: 30,
        hours4: 35,
        hours6: 45,
        night12: 35,
        night24: 40,
        extraHour: 12,
      },
    },
    {
      id: 3,
      name: 'Suite con Jacuzzi',
      capacity: 2,
      costPerStay: 15,
      rates: {
        hours2: 30,
        hours3: 35,
        hours4: 40,
        hours6: 50,
        night12: 40,
        night24: 45,
        extraHour: 15,
      },
    },
  ]);
  readonly equipment = signal<string[]>([
    'Wi-Fi',
    'Smart TV',
    'Aire acondicionado',
    'Agua caliente',
    'Cochera privada',
    'Sillón tántrico',
    'Jacuzzi',
    'Frigobar',
  ]);
  readonly catalog = signal<CatalogItem[]>([
    {
      id: 1,
      name: 'Desayuno buffet',
      category: 'Servicio',
      area: 'Restaurante',
      price: 28,
      active: true,
    },
    {
      id: 2,
      name: 'Spa · masaje',
      category: 'Servicio',
      area: 'Bienestar',
      price: 130,
      active: true,
    },
    {
      id: 3,
      name: 'Estacionamiento',
      category: 'Servicio',
      area: 'Cochera',
      price: 30,
      active: true,
    },
    { id: 4, name: 'Agua sin gas', category: 'Frigobar', area: 'Frigobar', price: 5, active: true },
    { id: 5, name: 'Gaseosa', category: 'Frigobar', area: 'Frigobar', price: 7, active: true },
    { id: 6, name: 'Snack', category: 'Vitrina', area: 'Recepción', price: 8, active: true },
  ]);
  readonly areas = signal<string[]>([
    'Restaurante',
    'Bienestar',
    'Cochera',
    'Frigobar',
    'Recepción',
  ]);
  readonly shifts = signal<ShiftConfig[]>([
    { id: 1, name: 'Mañana', start: '07:00', end: '15:00' },
    { id: 2, name: 'Tarde', start: '15:00', end: '23:00' },
    { id: 3, name: 'Noche', start: '23:00', end: '07:00' },
  ]);
  readonly paymentMethods = signal<PaymentMethod[]>([
    'Efectivo',
    'Yape',
    'Plin',
    'Tarjeta',
    'Transferencia',
  ]);
  readonly promotions = signal<Promotion[]>([
    { id: 1, name: 'Estadía larga', discount: 10, active: true },
  ]);
  readonly igv = signal(18);
  readonly ledger = signal<LedgerEntry[]>([
    {
      id: 1,
      date: day(0),
      hour: 8,
      description: 'Habitación 101 · noche',
      kind: 'Alojamiento',
      amount: 133,
      method: 'Efectivo',
      room: '101',
      roomType: 'Estándar',
      stayMode: 'Noche',
    },
    {
      id: 2,
      date: day(0),
      hour: 9,
      description: 'Habitación 103 · noche',
      kind: 'Alojamiento',
      amount: 280,
      method: 'Tarjeta',
      room: '103',
      roomType: 'Matrimonial',
      stayMode: 'Noche',
    },
    {
      id: 3,
      date: day(0),
      hour: 10,
      description: 'Habitación 202 · 3 horas',
      kind: 'Alojamiento',
      amount: 35,
      method: 'Yape',
      room: '202',
      roomType: 'Suite con Jacuzzi',
      stayMode: 'Horas',
    },
    {
      id: 4,
      date: day(0),
      hour: 11,
      description: 'Habitación 304 · 3 horas',
      kind: 'Alojamiento',
      amount: 35,
      method: 'Plin',
      room: '304',
      roomType: 'Suite con Jacuzzi',
      stayMode: 'Horas',
    },
    {
      id: 5,
      date: day(0),
      description: 'Desayunos habitación 101',
      kind: 'Consumo',
      amount: 56,
      method: 'Efectivo',
      room: '101',
      sourceId: 1,
    },
    {
      id: 6,
      date: day(0),
      description: 'Spa habitación 202',
      kind: 'Consumo',
      amount: 130,
      method: 'Yape',
      room: '202',
      sourceId: 2,
    },
    {
      id: 7,
      date: day(0),
      description: 'Estacionamiento habitación 103',
      kind: 'Consumo',
      amount: 30,
      method: 'Tarjeta',
      room: '103',
      sourceId: 3,
    },
    {
      id: 8,
      date: day(-1),
      description: 'Estadías y consumos',
      kind: 'Alojamiento',
      amount: 225,
      method: 'Efectivo',
      roomType: 'Estándar',
      stayMode: 'Noche',
    },
    {
      id: 9,
      date: day(-1),
      description: 'Alquiler por horas',
      kind: 'Alojamiento',
      amount: 145,
      method: 'Yape',
      roomType: 'Matrimonial',
      stayMode: 'Horas',
    },
    {
      id: 10,
      date: day(-2),
      description: 'Estadías',
      kind: 'Alojamiento',
      amount: 310,
      method: 'Tarjeta',
      roomType: 'Suite con Jacuzzi',
      stayMode: 'Noche',
    },
    {
      id: 11,
      date: day(-3),
      description: 'Alquiler por horas',
      kind: 'Alojamiento',
      amount: 175,
      method: 'Plin',
      roomType: 'Estándar',
      stayMode: 'Horas',
    },
    {
      id: 12,
      date: day(-4),
      description: 'Estadías',
      kind: 'Alojamiento',
      amount: 270,
      method: 'Transferencia',
      roomType: 'Matrimonial',
      stayMode: 'Noche',
    },
    {
      id: 13,
      date: day(-5),
      description: 'Estadías',
      kind: 'Alojamiento',
      amount: 195,
      method: 'Efectivo',
      roomType: 'Estándar',
      stayMode: 'Noche',
    },
    {
      id: 14,
      date: day(-6),
      description: 'Alquiler por horas',
      kind: 'Alojamiento',
      amount: 155,
      method: 'Yape',
      roomType: 'Suite con Jacuzzi',
      stayMode: 'Horas',
    },
    {
      id: 15,
      date: day(-9),
      description: 'Estadías',
      kind: 'Alojamiento',
      amount: 340,
      method: 'Tarjeta',
      roomType: 'Matrimonial',
      stayMode: 'Noche',
    },
    {
      id: 16,
      date: day(-16),
      description: 'Estadías',
      kind: 'Alojamiento',
      amount: 295,
      method: 'Efectivo',
      roomType: 'Suite con Jacuzzi',
      stayMode: 'Noche',
    },
  ]);
  readonly today = signal(day(0));
  readonly todayRevenue = computed(() =>
    this.ledger()
      .filter((entry) => entry.date === this.today() && entry.amount > 0)
      .reduce((sum, entry) => sum + entry.amount, 0),
  );

  addLedger(entry: Omit<LedgerEntry, 'id' | 'date'> & { date?: string }) {
    const id = Math.max(0, ...this.ledger().map((row) => row.id)) + 1;
    this.ledger.update((rows) => [
      { ...entry, id, date: entry.date || day(0), hour: entry.hour ?? new Date().getHours() },
      ...rows,
    ]);
    return id;
  }
  removeLedgerSource(sourceId: number) {
    this.ledger.update((rows) =>
      rows.filter((row) => row.kind !== 'Consumo' || row.sourceId !== sourceId),
    );
  }
  ratesFor(room: DemoRoom): RoomRates {
    return (
      room.customRates ||
      this.roomTypes().find((type) => type.name === room.type)?.rates ||
      this.roomTypes()[0].rates
    );
  }
  currencySymbol() {
    return this.profile().currency === 'PEN'
      ? 'S/'
      : this.profile().currency === 'USD'
        ? '$'
        : this.profile().currency;
  }
}
