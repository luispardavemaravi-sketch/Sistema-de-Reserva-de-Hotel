package pe.edu.utp.sistemadereservacionhotel.service.patron.exception;

public class DuplicadoException extends RuntimeException {
    public DuplicadoException(String mensaje) {
        super(mensaje);
    }
}