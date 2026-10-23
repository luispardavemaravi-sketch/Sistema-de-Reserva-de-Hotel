package pe.edu.utp.sistemadereservacionhotel.service.patron.estructural;


public class SpaDecorator extends ServicioDecorator {

    private static final double COSTO_SPA = 80.0;

    public SpaDecorator(ReservaDecorator reservaDecorator) {
        super(reservaDecorator);
    }

    @Override
    public double calcularCostoTotal() {
        return super.calcularCostoTotal() + COSTO_SPA;
    }

    @Override
    public String obtenerDescripcion() {
        return super.obtenerDescripcion() + " + Spa (S/. 80.00)";
    }
}