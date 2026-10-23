import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment';

export interface BackendRoom {
  id: number;
  numeroHabitacion: string;
  tipoHabitacion: string;
  precioActual: number;
  activo: boolean;
}
export interface BackendGuest {
  idHuesped: number;
  nombre: string;
  apellidos: string;
  documentoIdentidad: string;
  email: string;
  telefono: string;
  estadoActivo: boolean;
}
export interface BackendReservation {
  idReserva: number;
  codigoReserva: string;
  fechaEntradaPlanificada: string;
  fechaSalidaPlanificada: string;
  montoTotalEstimado: number;
  idHuesped: number;
  idHabitacion: number;
  estadoReserva: 'Pendiente' | 'Confirmada' | 'En curso' | 'Finalizada' | 'Cancelada';
}
export interface BackendReservationRequest {
  idHuesped: number;
  idHabitacion: number;
  fechaEntradaPlanificada: string;
  fechaSalidaPlanificada: string;
}

@Injectable({ providedIn: 'root' })
export class ReservaBackendService {
  private readonly http = inject(HttpClient);
  private readonly base = environment.apiUrl;
  rooms() { return this.http.get<BackendRoom[]>(`${this.base}/habitaciones`); }
  guests() { return this.http.get<BackendGuest[]>(`${this.base}/huespedes`); }
  reservations() { return this.http.get<BackendReservation[]>(`${this.base}/reservas`); }
  create(body: BackendReservationRequest) {
    return this.http.post<BackendReservation>(`${this.base}/reservas`, body);
  }
  update(id: number, body: BackendReservationRequest) {
    return this.http.put<BackendReservation>(`${this.base}/reservas/${id}`, body);
  }
  transition(id: number, action: 'confirmacion' | 'entrada' | 'salida') {
    return this.http.post<BackendReservation>(`${this.base}/reservas/${id}/${action}`, {});
  }
  cancel(id: number) { return this.http.delete<void>(`${this.base}/reservas/${id}`); }
}
