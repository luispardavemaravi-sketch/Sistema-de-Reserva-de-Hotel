package pe.edu.utp.sistemadereservacionhotel.service.patron.creacional;

import pe.edu.utp.sistemadereservacionhotel.model.finanzas.ComprobantePago;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.Reserva;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

public class FacturaFactory implements ComprobanteFactory {

    private static final BigDecimal IGV = new BigDecimal("1.18");

    @Override
    public ComprobantePago crear(Reserva reserva, BigDecimal montoTotal) {
        ComprobantePago factura = new ComprobantePago();

        factura.setNumeroSerie("F001-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        factura.setFechaEmision(LocalDateTime.now(ZoneId.of("America/Lima")));
        factura.setReserva(reserva);

        // Operación matemática segura con BigDecimal
        BigDecimal montoConIgv = montoTotal.multiply(IGV).setScale(2, RoundingMode.HALF_UP);
        factura.setMontoTotal(montoConIgv);

        return factura;
    }
}
