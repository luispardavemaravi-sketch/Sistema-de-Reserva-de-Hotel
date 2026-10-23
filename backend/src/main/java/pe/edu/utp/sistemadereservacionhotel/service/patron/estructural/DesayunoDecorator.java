package pe.edu.utp.sistemadereservacionhotel.service.patron.estructural;


public class DesayunoDecorator extends ServicioDecorator {

    private static final double COSTO_DESAYUNO = 25.0;

    public DesayunoDecorator(ReservaDecorator reservaDecorator) {
        super(reservaDecorator);
    }

    @Override
    public double calcularCostoTotal() {
        return super.calcularCostoTotal() + COSTO_DESAYUNO;
    }

    @Override
    public String obtenerDescripcion() {
        return super.obtenerDescripcion() + " + Desayuno (S/. 25.00)";
    }
}