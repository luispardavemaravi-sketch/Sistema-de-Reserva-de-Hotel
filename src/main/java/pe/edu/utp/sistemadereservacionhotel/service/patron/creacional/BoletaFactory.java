package pe.edu.utp.sistemadereservacionhotel.service.patron.creacional;

import pe.edu.utp.sistemadereservacionhotel.model.finanzas.ComprobantePago;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.Reserva;
import pe.edu.utp.sistemadereservacionhotel.service.patron.singleton.UtilSingleton;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

public class BoletaFactory implements ComprobanteFactory {

    @Override
    public ComprobantePago crear(Reserva reserva, Double montoTotal) {
        ComprobantePago boleta = new ComprobantePago();

        boleta.setNumeroSerie("B001-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        boleta.setFechaEmision(LocalDateTime.now(ZoneId.of("America/Lima")));
        boleta.setReserva(reserva);

        double montoConIgv = UtilSingleton.calcularMontoConImpuesto(montoTotal);
        boleta.setMontoTotal(montoConIgv);

        return boleta;
    }
}