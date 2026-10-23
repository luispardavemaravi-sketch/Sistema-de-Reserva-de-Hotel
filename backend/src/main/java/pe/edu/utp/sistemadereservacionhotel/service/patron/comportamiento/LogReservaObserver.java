// Observer concreto: Log del sistema
package pe.edu.utp.sistemadereservacionhotel.service.patron.comportamiento;

import pe.edu.utp.sistemadereservacionhotel.model.reserva.Reserva;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class LogReservaObserver implements ReservaObserver {

    @Override
    public void actualizar(Reserva reserva, String nuevoEstado) {
        System.out.printf(
                "[LOG SISTEMA] %s - Reserva ID: %d | Código: %s | Nuevo estado: %s%n",
                LocalDateTime.now(ZoneId.of("America/Lima")),
                reserva.getIdReserva(),
                reserva.getCodigoReserva(),
                nuevoEstado
        );
    }
}