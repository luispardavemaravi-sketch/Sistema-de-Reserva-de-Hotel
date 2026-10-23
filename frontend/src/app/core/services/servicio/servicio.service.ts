import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { CatalogoServicioDTO } from '../../models/servicio.model';

@Injectable({ providedIn: 'root' })
export class ServicioService {
  private readonly http = inject(HttpClient);
  private readonly url = `${environment.apiUrl}/servicios`;
  getAll() {
    return this.http.get<CatalogoServicioDTO[]>(this.url);
  }
  getById(id: number) {
    return this.http.get<CatalogoServicioDTO>(`${this.url}/${id}`);
  }
  create(body: Omit<CatalogoServicioDTO, 'id'>) {
    return this.http.post<CatalogoServicioDTO>(this.url, body);
  }
  update(id: number, body: Omit<CatalogoServicioDTO, 'id'>) {
    return this.http.put<CatalogoServicioDTO>(`${this.url}/${id}`, body);
  }
  delete(id: number) {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
