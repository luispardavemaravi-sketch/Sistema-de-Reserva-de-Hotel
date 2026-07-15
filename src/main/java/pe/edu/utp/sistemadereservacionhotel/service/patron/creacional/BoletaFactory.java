package pe.edu.utp.sistemadereservacionhotel.service.patron.creacional;

import pe.edu.utp.sistemadereservacionhotel.model.finanzas.ComprobantePago;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.Reserva;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

public class BoletaFactory implements ComprobanteFactory {

    @Override
    public ComprobantePago crear(Reserva reserva, BigDecimal montoTotal) {
        ComprobantePago boleta = new ComprobantePago();

        boleta.setNumeroSerie("B001-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        boleta.setFechaEmision(LocalDateTime.now(ZoneId.of("America/Lima")));
        boleta.setReserva(reserva);

        // La boleta para consumidor final ya tiene los impuestos incluidos en el diseño,
        // a diferencia de la factura que requiere desglose. Asignamos el total directo.
        boleta.setMontoTotal(montoTotal);

        return boleta;
    }
}