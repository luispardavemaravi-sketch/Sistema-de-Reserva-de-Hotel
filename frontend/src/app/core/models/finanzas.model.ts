export type TipoComprobante = 'BOLETA' | 'FACTURA';
export interface CajaAperturaDTO {
  empleadoId: number;
  montoInicial: number;
  observaciones?: string;
}
export interface CajaResponseDTO {
  id: number;
  empleadoId: number;
  fechaApertura: string;
  fechaCierre?: string | null;
  montoInicial: number;
  montoFinal?: number | null;
  estado: 'ABIERTA' | 'CERRADA';
}
export interface DetalleComprobanteRequestDTO {
  descripcion: string;
  cantidad: number;
  precioUnitario: number;
  impuestoId?: number;
}
export interface DetalleComprobanteResponseDTO extends DetalleComprobanteRequestDTO {
  id: number;
  subtotal: number;
  total: number;
}
export interface ComprobanteRequestDTO {
  tipo: TipoComprobante;
  huespedId: number;
  reservaId?: number;
  metodoPagoId: number;
  razonSocial?: string;
  ruc?: string;
  detalles: DetalleComprobanteRequestDTO[];
  promocionId?: number;
}
export interface ComprobanteResponseDTO extends ComprobanteRequestDTO {
  id: number;
  serie: string;
  numero: string;
  fechaEmision: string;
  subtotal: number;
  impuestos: number;
  total: number;
  detalles: DetalleComprobanteResponseDTO[];
}
export interface ImpuestoDTO {
  id: number;
  nombre: string;
  porcentaje: number;
  activo: boolean;
}
export interface MetodoPagoDTO {
  id: number;
  nombre: string;
  activo: boolean;
}
export interface PromocionDTO {
  id: number;
  nombre: string;
  descripcion?: string;
  porcentajeDescuento: number;
  fechaInicio: string;
  fechaFin: string;
  activa: boolean;
}
