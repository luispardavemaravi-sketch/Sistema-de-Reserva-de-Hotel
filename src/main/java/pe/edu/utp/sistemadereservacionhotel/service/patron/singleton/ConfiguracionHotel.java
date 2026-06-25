package pe.edu.utp.sistemadereservacionhotel.service.patron.singleton;

public class ConfiguracionHotel {
    private static ConfiguracionHotel instance;
    private double porcentajeImpuesto;

    private ConfiguracionHotel() {
        this.porcentajeImpuesto = 0.18; // Valor por defecto, puede ser modificado
    }

    public synchronized static ConfiguracionHotel getInstance() {
        if (instance == null) {
            instance = new ConfiguracionHotel();
        }
        return instance;
    }

    public double getPorcentajeImpuesto() {
        return porcentajeImpuesto;
    }

}
