package pe.edu.utp.sistemadereservacionhotel.service.patron.comportamiento;

import pe.edu.utp.sistemadereservacionhotel.model.reserva.Reserva;

public interface ReservaObserver {
    void actualizar(Reserva reserva, String nuevoEstado);
}
