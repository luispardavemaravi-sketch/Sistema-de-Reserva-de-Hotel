import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { ReservaRequestDTO, ReservaResponseDTO } from '../../models/reserva.model';

@Injectable({ providedIn: 'root' })
export class ReservaService {
  private readonly http = inject(HttpClient);
  private readonly url = `${environment.apiUrl}/reservas`;
  getAll() {
    return this.http.get<ReservaResponseDTO[]>(this.url);
  }
  getById(id: number) {
    return this.http.get<ReservaResponseDTO>(`${this.url}/${id}`);
  }
  create(body: ReservaRequestDTO) {
    return this.http.post<ReservaResponseDTO>(this.url, body);
  }
  update(id: number, body: ReservaRequestDTO) {
    return this.http.put<ReservaResponseDTO>(`${this.url}/${id}`, body);
  }
  delete(id: number) {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
