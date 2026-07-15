package pe.edu.utp.sistemadereservacionhotel.model.reserva;

/**
 * Enumeración que define los vínculos relacionales permitidos para los acompañantes.
 * Garantiza la integridad de los datos y permite indexación eficiente en base de datos.
 */
public enum Parentesco {
    CONYUGE,
    HIJO_A,
    PADRE_MADRE,
    HERMANO_A,
    AMIGO_A,
    COLEGA,
    OTRO
}