export interface HuespedRequestDTO {
  nombres: string;
  apellidos: string;
  tipoDocumento: string;
  numeroDocumento: string;
  correo?: string;
  telefono?: string;
  nacionalidad?: string;
  direccion?: string;
}
export interface HuespedResponseDTO extends HuespedRequestDTO {
  id: number;
  fechaRegistro?: string;
}
