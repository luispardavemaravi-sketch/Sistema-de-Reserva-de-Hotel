import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { EmpleadoRequestDTO, EmpleadoResponseDTO } from '../../models/personal.model';

export interface BackendTurno {
  idTurno: number;
  horaInicio: string;
  horaFinal: string;
  diasSemana: string[];
}

@Injectable({ providedIn: 'root' })
export class PersonalService {
  private readonly http = inject(HttpClient);
  private readonly url = `${environment.apiUrl}/empleados`;
  getTurnos() {
    return this.http.get<BackendTurno[]>(`${environment.apiUrl}/personal/turnos`);
  }
  getAll() {
    return this.http.get<EmpleadoResponseDTO[]>(this.url);
  }
  getById(id: number) {
    return this.http.get<EmpleadoResponseDTO>(`${this.url}/${id}`);
  }
  create(body: EmpleadoRequestDTO) {
    return this.http.post<EmpleadoResponseDTO>(this.url, body);
  }
  update(id: number, body: EmpleadoRequestDTO) {
    return this.http.put<EmpleadoResponseDTO>(`${this.url}/${id}`, body);
  }
  delete(id: number) {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
