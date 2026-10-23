/** Contratos propuestos; ajustar nombres y nulabilidad con los DTO reales de Spring Boot. */
export interface PisoDTO {
  id: number;
  numero: number;
  nombre: string;
  activo: boolean;
}
export interface TipoHabitacionDTO {
  id: number;
  nombre: string;
  descripcion?: string;
  capacidad: number;
}
export interface EstadoHabitacionDTO {
  id: number;
  nombre: 'Disponible' | 'Ocupada' | 'Limpieza' | 'Mantenimiento' | string;
}
export interface EquipamientoDTO {
  id: number;
  nombre: string;
  descripcion?: string;
}
export interface PrecioHabitacionDTO {
  id: number;
  tipoHabitacionId: number;
  precioNoche: number;
  moneda: string;
  vigenteDesde: string;
  vigenteHasta?: string | null;
}
export interface HabitacionRequestDTO {
  numero: string;
  pisoId: number;
  tipoHabitacionId: number;
  estadoHabitacionId: number;
  equipamientoIds: number[];
  observaciones?: string;
}
export interface HabitacionResponseDTO extends HabitacionRequestDTO {
  id: number;
  piso: PisoDTO;
  tipoHabitacion: TipoHabitacionDTO;
  estadoHabitacion: EstadoHabitacionDTO;
  equipamiento: EquipamientoDTO[];
  precioActual?: PrecioHabitacionDTO | null;
}
