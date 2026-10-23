import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { ComprobanteRequestDTO, ComprobanteResponseDTO } from '../../models/finanzas.model';

@Injectable({ providedIn: 'root' })
export class FinanzasService {
  private readonly http = inject(HttpClient);
  private readonly url = `${environment.apiUrl}/comprobantes`;
  getAll() {
    return this.http.get<ComprobanteResponseDTO[]>(this.url);
  }
  getById(id: number) {
    return this.http.get<ComprobanteResponseDTO>(`${this.url}/${id}`);
  }
  create(body: ComprobanteRequestDTO) {
    return this.http.post<ComprobanteResponseDTO>(this.url, body);
  }
  update(id: number, body: ComprobanteRequestDTO) {
    return this.http.put<ComprobanteResponseDTO>(`${this.url}/${id}`, body);
  }
  delete(id: number) {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
