package pe.edu.utp.sistemadereservacionhotel.service.patron.creacional;

import pe.edu.utp.sistemadereservacionhotel.model.finanzas.ComprobantePago;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.Reserva;
import pe.edu.utp.sistemadereservacionhotel.service.patron.singleton.UtilSingleton;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

public class FacturaFactory implements ComprobanteFactory {

    @Override
    public ComprobantePago crear(Reserva reserva, Double montoTotal) {
        ComprobantePago factura = new ComprobantePago();

        factura.setNumeroSerie("F001-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        factura.setFechaEmision(LocalDateTime.now(ZoneId.of("America/Lima")));
        factura.setReserva(reserva);

        double montoConIgv = UtilSingleton.calcularMontoConImpuesto(montoTotal);
        factura.setMontoTotal(montoConIgv);

        return factura;
    }
}