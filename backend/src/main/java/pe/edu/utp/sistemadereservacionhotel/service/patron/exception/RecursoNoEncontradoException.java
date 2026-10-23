package pe.edu.utp.sistemadereservacionhotel.service.patron.exception;

/**
 * Excepción personalizada para manejar la ausencia de recursos en la capa de persistencia.
 * Utiliza sobrecarga de constructores para adaptarse tanto a búsquedas por clave primaria
 * como por criterios de negocio (código de reserva, email, etc.).
 */
public class RecursoNoEncontradoException extends RuntimeException {

    // Constructor base para mensajes genéricos
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }

    // Constructor para búsquedas por ID (tipo Long)
    public RecursoNoEncontradoException(String entidad, Long id) {
        super(entidad + " no encontrado con ID: " + id);
    }

    // Constructor para búsquedas por Criterio de negocio (tipo String, ej. Código de Reserva)
    public RecursoNoEncontradoException(String entidad, String criterio) {
        super(entidad + " no encontrado con criterio: " + criterio);
    }
}