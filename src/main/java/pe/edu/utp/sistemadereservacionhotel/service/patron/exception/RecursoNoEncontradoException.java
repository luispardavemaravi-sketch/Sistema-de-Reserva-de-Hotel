package pe.edu.utp.sistemadereservacionhotel.service.patron.exception;

public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }

    public RecursoNoEncontradoException(String recurso, Long id) {
        super(recurso + " no encontrado con ID: " + id);
    }
}