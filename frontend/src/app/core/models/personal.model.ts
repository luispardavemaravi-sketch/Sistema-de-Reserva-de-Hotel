export enum CargoEmpleado {
  ADMINISTRADOR = 'ADMINISTRADOR',
  RECEPCIONISTA = 'RECEPCIONISTA',
  LIMPIEZA = 'LIMPIEZA',
  MANTENIMIENTO = 'MANTENIMIENTO',
  CAJERO = 'CAJERO',
  SERVICIOS = 'SERVICIOS',
}
export enum EspecialidadEmpleado {
  RECEPCION = 'RECEPCION',
  FINANZAS = 'FINANZAS',
  HOUSEKEEPING = 'HOUSEKEEPING',
  MANTENIMIENTO = 'MANTENIMIENTO',
  GASTRONOMIA = 'GASTRONOMIA',
  SPA = 'SPA',
}
export interface TurnoDTO {
  id: number;
  nombre: string;
  horaInicio: string;
  horaFin: string;
}
export interface EmpleadoRequestDTO {
  nombres: string;
  apellidos: string;
  numeroDocumento: string;
  correo: string;
  telefono?: string;
  cargo: CargoEmpleado;
  especialidad?: EspecialidadEmpleado;
  turnoId?: number;
  activo: boolean;
}
export interface EmpleadoResponseDTO extends EmpleadoRequestDTO {
  id: number;
  turno?: TurnoDTO | null;
}
