package pe.edu.utp.sistemadereservacionhotel.service.patron.comportamiento;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.Reserva;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaEstadoSubjectTest {

    @Mock
    private Reserva reservaMock;

    @Mock
    private ReservaObserver observer1;

    @Mock
    private ReservaObserver observer2;

    @Test
    void notificarObservers_ConMultiplesObservers_TodosSonNotificados() {
        // Arrange
        ReservaEstadoSubject subject = new ReservaEstadoSubject(reservaMock);
        subject.agregarObserver(observer1);
        subject.agregarObserver(observer2);

        // Act
        subject.notificarObservers("CONFIRMADA");

        // Assert: Verificamos que ambos observadores recibieron el mensaje exactamente 1 vez
        verify(observer1, times(1)).actualizar(reservaMock, "CONFIRMADA");
        verify(observer2, times(1)).actualizar(reservaMock, "CONFIRMADA");
    }

    @Test
    void eliminarObserver_LuegoDeNotificar_NoRecibeNuevasNotificaciones() {
        // Arrange
        ReservaEstadoSubject subject = new ReservaEstadoSubject(reservaMock);
        subject.agregarObserver(observer1);
        subject.agregarObserver(observer2);

        // Act: Eliminamos al observer1 ANTES de notificar
        subject.eliminarObserver(observer1);
        subject.notificarObservers("CANCELADA");

        // Assert: El observer1 nunca debió enterarse, el observer2 sí
        verify(observer1, never()).actualizar(any(), any());
        verify(observer2, times(1)).actualizar(reservaMock, "CANCELADA");
    }
}