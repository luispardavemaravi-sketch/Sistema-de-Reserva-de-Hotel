export enum CategoriaServicio {
  ALIMENTACION = 'ALIMENTACION',
  BIENESTAR = 'BIENESTAR',
  TRANSPORTE = 'TRANSPORTE',
  LAVANDERIA = 'LAVANDERIA',
  OTROS = 'OTROS',
}
export interface AreaHotelDTO {
  id: number;
  nombre: string;
  descripcion?: string;
  activa: boolean;
}
export interface CatalogoServicioDTO {
  id: number;
  nombre: string;
  descripcion?: string;
  categoria: CategoriaServicio;
  areaId: number;
  precio: number;
  activo: boolean;
}
export interface ConsumoServicioDTO {
  id: number;
  reservaId: number;
  habitacionId: number;
  servicioId: number;
  cantidad: number;
  precioUnitario: number;
  total: number;
  fechaHora: string;
}
export interface ServicioAdicionalDTO {
  id: number;
  nombre: string;
  categoria: CategoriaServicio;
  precio: number;
  descripcion?: string;
}
