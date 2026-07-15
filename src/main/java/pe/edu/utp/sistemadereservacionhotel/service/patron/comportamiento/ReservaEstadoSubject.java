// Implementación Subject
package pe.edu.utp.sistemadereservacionhotel.service.patron.comportamiento;

import pe.edu.utp.sistemadereservacionhotel.model.reserva.Reserva;

import java.util.ArrayList;
import java.util.List;

public class ReservaEstadoSubject implements ReservaSubject {

    private final List<ReservaObserver> observers = new ArrayList<>();
    private final Reserva reserva;

    public ReservaEstadoSubject(Reserva reserva) {
        this.reserva = reserva;
    }

    @Override
    public void agregarObserver(ReservaObserver observer) {
        observers.add(observer);
    }

    @Override
    public void eliminarObserver(ReservaObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notificarObservers(String nuevoEstado) {
        for (ReservaObserver observer : observers) {
            observer.actualizar(reserva, nuevoEstado);
        }
    }
}