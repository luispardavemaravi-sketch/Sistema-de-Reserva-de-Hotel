package pe.edu.utp.sistemadereservacionhotel.service.patron.estructural;

public abstract class ServicioDecorator implements ReservaDecorator {

    protected final ReservaDecorator reservaDecorator;

    protected ServicioDecorator(ReservaDecorator reservaDecorator) {
        this.reservaDecorator = reservaDecorator;
    }

    @Override
    public double calcularCostoTotal() {
        return reservaDecorator.calcularCostoTotal();
    }

    @Override
    public String obtenerDescripcion() {
        return reservaDecorator.obtenerDescripcion();
    }
}