// Interfaz Subject
package pe.edu.utp.sistemadereservacionhotel.service.patron.comportamiento;

public interface ReservaSubject {
    void agregarObserver(ReservaObserver observer);

    void eliminarObserver(ReservaObserver observer);

    void notificarObservers(String nuevoEstado);
}