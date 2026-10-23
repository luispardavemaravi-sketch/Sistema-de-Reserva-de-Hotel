package pe.edu.utp.sistemadereservacionhotel.service.patron.exception;

public class ValidacionException extends RuntimeException {
    public ValidacionException(String mensaje) {
        super(mensaje);
    }
}