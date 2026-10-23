package pe.edu.utp.sistemadereservacionhotel.service.patron.creacional;

import pe.edu.utp.sistemadereservacionhotel.model.finanzas.ComprobantePago;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.Reserva;

import java.math.BigDecimal;

public interface ComprobanteFactory {
    ComprobantePago crear(Reserva reserva, BigDecimal montoTotal);
}