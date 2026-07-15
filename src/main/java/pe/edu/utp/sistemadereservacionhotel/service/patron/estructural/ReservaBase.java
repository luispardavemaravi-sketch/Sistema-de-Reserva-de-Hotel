package pe.edu.utp.sistemadereservacionhotel.service.patron.estructural;

import pe.edu.utp.sistemadereservacionhotel.model.reserva.Reserva;

public class ReservaBase implements ReservaDecorator {

    private final Reserva reserva;

    public ReservaBase(Reserva reserva) {
        this.reserva = reserva;
    }

    @Override
    public double calcularCostoTotal() {
        // Si el monto es nulo, retorna 0.0, si no convierte el BigDecimal a double
        return reserva.getMontoTotalEstimado() != null
                ? reserva.getMontoTotalEstimado().doubleValue()
                : 0.0;
    }

    @Override
    public String obtenerDescripcion() {
        return "Reserva base: " + reserva.getCodigoReserva();
    }
}