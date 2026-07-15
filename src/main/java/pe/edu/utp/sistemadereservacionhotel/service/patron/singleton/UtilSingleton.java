package pe.edu.utp.sistemadereservacionhotel.service.patron.singleton;

public class UtilSingleton {
    private UtilSingleton() {
        throw new UnsupportedOperationException("Clase de utilidad");
    }
    public static double calcularImpuesto(double monto) {
        ConfiguracionHotel config = ConfiguracionHotel.getInstance();
        return monto * config.getPorcentajeImpuesto();
    }

    public static double calcularMontoConImpuesto(double monto) {
        return monto + calcularImpuesto(monto);
    }

    public static boolean esHoraValidaCheckIn(int hora) {
        return hora >= ConfiguracionHotel.getInstance().getHoraCheckIn();
    }

    public static boolean esHoraValidaCheckOut(int hora) {
        return hora <= ConfiguracionHotel.getInstance().getHoraCheckOut();
    }

    public static String obtenerInfoHotel() {
        ConfiguracionHotel config = ConfiguracionHotel.getInstance();
        return String.format("Hotel: %s | Moneda: %s | IGV: %.0f%%",
                config.getNombreHotel(),
                config.getMoneda(),
                config.getPorcentajeImpuesto() * 100);
    }
}