// Observer concreto: Notificación al Huésped
package pe.edu.utp.sistemadereservacionhotel.service.patron.comportamiento;

import pe.edu.utp.sistemadereservacionhotel.model.reserva.Reserva;

public class NotificacionHuespedObserver implements ReservaObserver {

    @Override
    public void actualizar(Reserva reserva, String nuevoEstado) {
        System.out.printf(
                "[NOTIFICACIÓN HUÉSPED] Estimado %s %s, su reserva %s ha cambiado al estado: %s%n",
                reserva.getHuesped().getNombre(),
                reserva.getHuesped().getApellidos(),
                reserva.getCodigoReserva(),
                nuevoEstado
        );
    }
}