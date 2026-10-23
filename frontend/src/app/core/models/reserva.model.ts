export enum Parentesco {
  CONYUGE = 'CONYUGE',
  HIJO = 'HIJO',
  PADRE = 'PADRE',
  MADRE = 'MADRE',
  HERMANO = 'HERMANO',
  FAMILIAR = 'FAMILIAR',
  OTRO = 'OTRO',
}
export interface EstadoReservaDTO {
  id: number;
  nombre: 'Pendiente' | 'Confirmada' | 'En curso' | 'Finalizada' | 'Cancelada' | string;
}
export interface AcompananteRequestDTO {
  nombres: string;
  apellidos: string;
  tipoDocumento: string;
  numeroDocumento: string;
  parentesco: Parentesco;
}
export interface AcompananteResponseDTO extends AcompananteRequestDTO {
  id: number;
  reservaId: number;
}
/** Extras modelados como cargos para desayunos, estacionamiento, spa y futuros decoradores. */
export interface ExtraReservaDTO {
  codigo: 'DESAYUNO' | 'ESTACIONAMIENTO' | 'SPA' | string;
  nombre: string;
  cantidad: number;
  precioUnitario: number;
}
export interface ReservaRequestDTO {
  huespedId: number;
  habitacionId: number;
  fechaEntrada: string;
  fechaSalida: string;
  numeroAdultos: number;
  numeroNinos: number;
  acompanantes?: AcompananteRequestDTO[];
  extras?: ExtraReservaDTO[];
  observaciones?: string;
}
export interface ReservaResponseDTO extends ReservaRequestDTO {
  id: number;
  codigo?: string;
  estado: EstadoReservaDTO;
  total: number;
  acompanantes?: AcompananteResponseDTO[];
}
export interface CheckInRequestDTO {
  reservaId: number;
  fechaHora?: string;
  observaciones?: string;
}
export interface CheckInResponseDTO {
  id: number;
  reservaId: number;
  fechaHora: string;
  habitacionId: number;
}
export interface CheckOutRequestDTO {
  reservaId: number;
  fechaHora?: string;
  observaciones?: string;
}
export interface CheckOutResponseDTO {
  id: number;
  reservaId: number;
  fechaHora: string;
  totalConsumos: number;
  totalPagar: number;
}
