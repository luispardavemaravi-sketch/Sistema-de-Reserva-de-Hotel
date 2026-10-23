import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { HabitacionRequestDTO, HabitacionResponseDTO } from '../../models/habitacion.model';

@Injectable({ providedIn: 'root' })
export class HabitacionService {
  private readonly http = inject(HttpClient);
  private readonly url = `${environment.apiUrl}/habitaciones`;
  getAll() {
    return this.http.get<HabitacionResponseDTO[]>(this.url);
  }
  getById(id: number) {
    return this.http.get<HabitacionResponseDTO>(`${this.url}/${id}`);
  }
  create(body: HabitacionRequestDTO) {
    return this.http.post<HabitacionResponseDTO>(this.url, body);
  }
  update(id: number, body: HabitacionRequestDTO) {
    return this.http.put<HabitacionResponseDTO>(`${this.url}/${id}`, body);
  }
  delete(id: number) {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
