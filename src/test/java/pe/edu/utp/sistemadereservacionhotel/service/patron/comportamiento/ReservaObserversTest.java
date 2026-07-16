package pe.edu.utp.sistemadereservacionhotel.service.patron.comportamiento;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pe.edu.utp.sistemadereservacionhotel.model.huesped.Huesped;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.Reserva;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReservaObserversTest {

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream standardOut = System.out;

    @BeforeEach
    public void setUp() {
        // Redirigimos la salida de la consola a nuestro capturador antes de cada test
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        // Restauramos la consola original para no afectar otros tests
        System.setOut(standardOut);
    }

    @Test
    void notificacionHuespedObserver_GeneraMensajeConDatosDelHuesped() {
        // Arrange
        NotificacionHuespedObserver observer = new NotificacionHuespedObserver();

        Reserva reservaMock = mock(Reserva.class);
        Huesped huespedMock = mock(Huesped.class);

        when(reservaMock.getHuesped()).thenReturn(huespedMock);
        when(huespedMock.getNombre()).thenReturn("Carlos");
        when(huespedMock.getApellidos()).thenReturn("Salazar");
        when(reservaMock.getCodigoReserva()).thenReturn("RES-999");

        // Act
        observer.actualizar(reservaMock, "CHECK_IN");

        // Assert
        String salidaConsola = outputStreamCaptor.toString().trim();
        assertTrue(salidaConsola.contains("Estimado Carlos Salazar"), "El mensaje no contiene el nombre del huésped");
        assertTrue(salidaConsola.contains("RES-999"), "El mensaje no contiene el código de la reserva");
        assertTrue(salidaConsola.contains("CHECK_IN"), "El mensaje no contiene el nuevo estado");
    }

    @Test
    void logReservaObserver_GeneraLogConFormatoCorrecto() {
        // Arrange
        LogReservaObserver observer = new LogReservaObserver();
        Reserva reservaMock = mock(Reserva.class);

        when(reservaMock.getIdReserva()).thenReturn(50L);
        when(reservaMock.getCodigoReserva()).thenReturn("RES-050");

        // Act
        observer.actualizar(reservaMock, "FINALIZADA");

        // Assert
        String salidaConsola = outputStreamCaptor.toString().trim();
        assertTrue(salidaConsola.contains("[LOG SISTEMA]"), "Falta la etiqueta del log");
        assertTrue(salidaConsola.contains("ID: 50"), "Falta el ID de la reserva");
        assertTrue(salidaConsola.contains("FINALIZADA"), "Falta el estado finalizado");
    }
}