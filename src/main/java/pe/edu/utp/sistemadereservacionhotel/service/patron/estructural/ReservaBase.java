package pe.edu.utp.sistemadereservacionhotel.service.patron.estructural;

import pe.edu.utp.sistemadereservacionhotel.model.reserva.Reserva;

public class ReservaBase implements ReservaDecorator {

    private final Reserva reserva;

    public ReservaBase(Reserva reserva) {
        this.reserva = reserva;
    }

    @Override
    public double calcularCostoTotal() {
        return reserva.getMontoTotalEstimado() != null
                ? reserva.getMontoTotalEstimado()
                : 0.0;
    }

    @Override
    public String obtenerDescripcion() {
        return "Reserva base: " + reserva.getCodigoReserva();
    }
}