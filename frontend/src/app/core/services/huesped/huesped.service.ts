import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { HuespedRequestDTO, HuespedResponseDTO } from '../../models/huesped.model';

@Injectable({ providedIn: 'root' })
export class HuespedService {
  private readonly http = inject(HttpClient);
  private readonly url = `${environment.apiUrl}/huespedes`;
  getAll() {
    return this.http.get<HuespedResponseDTO[]>(this.url);
  }
  getById(id: number) {
    return this.http.get<HuespedResponseDTO>(`${this.url}/${id}`);
  }
  create(body: HuespedRequestDTO) {
    return this.http.post<HuespedResponseDTO>(this.url, body);
  }
  update(id: number, body: HuespedRequestDTO) {
    return this.http.put<HuespedResponseDTO>(`${this.url}/${id}`, body);
  }
  delete(id: number) {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
