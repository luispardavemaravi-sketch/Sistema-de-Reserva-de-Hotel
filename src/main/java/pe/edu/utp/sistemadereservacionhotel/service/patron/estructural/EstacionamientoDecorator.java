package pe.edu.utp.sistemadereservacionhotel.service.patron.estructural;


public class EstacionamientoDecorator extends ServicioDecorator {

    private static final double COSTO_ESTACIONAMIENTO = 15.0;

    public EstacionamientoDecorator(ReservaDecorator reservaDecorator) {
        super(reservaDecorator);
    }

    @Override
    public double calcularCostoTotal() {
        return super.calcularCostoTotal() + COSTO_ESTACIONAMIENTO;
    }

    @Override
    public String obtenerDescripcion() {
        return super.obtenerDescripcion() + " + Estacionamiento (S/. 15.00)";
    }
}