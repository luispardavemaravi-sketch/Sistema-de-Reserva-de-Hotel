package pe.edu.utp.sistemadereservacionhotel.service.patron.singleton;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class ConfiguracionHotel {
    private static volatile ConfiguracionHotel instance;

    private double porcentajeImpuesto;
    private String nombreHotel;
    private String moneda;
    private int horaCheckIn;
    private int horaCheckOut;

    private ConfiguracionHotel() {
        this.porcentajeImpuesto = 0.18;
        this.nombreHotel = "Hotel Plaza";
        this.moneda = "PEN";
        this.horaCheckIn = 14;
        this.horaCheckOut = 12;
    }

    public static ConfiguracionHotel getInstance() {
        if (instance == null) {
            synchronized (ConfiguracionHotel.class) {
                if (instance == null) {
                    instance = new ConfiguracionHotel();
                }
            }
        }
        return instance;
    }

    public double getPorcentajeImpuesto() { return porcentajeImpuesto; }
    public String getNombreHotel() { return nombreHotel; }
    public String getMoneda() { return moneda; }
    public int getHoraCheckIn() { return horaCheckIn; }
    public int getHoraCheckOut() { return horaCheckOut; }


}