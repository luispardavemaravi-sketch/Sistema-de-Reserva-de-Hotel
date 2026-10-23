export interface HuespedRequestDTO {
  nombre: string;
  apellidos: string;
  documentoIdentidad: string;
  email: string;
  telefono: string;
}

export interface HuespedResponseDTO extends HuespedRequestDTO {
  idHuesped: number;
  estadoActivo: boolean;
}
