package pe.edu.utp.sistemadereservacionhotel.service.patron.creacional;

import pe.edu.utp.sistemadereservacionhotel.model.finanzas.ComprobantePago;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.Reserva;

public interface ComprobanteFactory {
    ComprobantePago crear(Reserva reserva, Double montoTotal);
}