package pe.edu.utp.sistemadereservacionhotel.service.patron.creacional;

import org.junit.jupiter.api.Test;
import pe.edu.utp.sistemadereservacionhotel.model.habitacion.Habitacion;
import pe.edu.utp.sistemadereservacionhotel.model.huesped.Huesped;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.EstadoReserva;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.Reserva;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Suite de pruebas para validar la inmutabilidad condicional y ensamblaje del ReservaBuilder.
 */
class ReservaBuilderTest {

    @Test
    void build_ConDatosCompletos_EnsamblaReservaCorrectamente() {
        // Arrange
        LocalDate manana = LocalDate.now().plusDays(1);
        LocalDate pasadoManana = LocalDate.now().plusDays(2);

        // Act
        Reserva reserva = ReservaBuilder.builder()
                .codigoReserva("RES-999")
                .fechaEntradaPlanificada(manana)
                .fechaSalidaPlanificada(pasadoManana)
                .montoTotalEstimado(new BigDecimal("500.00"))
                .huesped(new Huesped())
                .habitacion(new Habitacion())
                .estadoReserva(new EstadoReserva())
                .build();

        // Assert
        assertNotNull(reserva, "El Builder devolvió una reserva nula");
        assertEquals("RES-999", reserva.getCodigoReserva(), "Fallo en la asignación del código");
        assertEquals(new BigDecimal("500.00"), reserva.getMontoTotalEstimado(), "Fallo en la asignación del monto");
    }

    @Test
    void build_FaltanDatosObligatorios_LanzaIllegalStateException() {
        // Act & Assert: Intentar hacer build() sin los datos requeridos
        assertThrows(IllegalStateException.class, () -> {
            ReservaBuilder.builder()
                    .codigoReserva("RES-000") // Solo enviamos un dato
                    .build();
        }, "El Builder permitió ensamblar una reserva con campos obligatorios nulos");
    }

    @Test
    void fechaEntradaPlanificada_FechaEnElPasado_LanzaIllegalArgumentException() {
        // Arrange
        LocalDate ayer = LocalDate.now().minusDays(1);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            ReservaBuilder.builder().fechaEntradaPlanificada(ayer);
        }, "El Builder no bloqueó el ingreso de una fecha de entrada en el pasado");
    }
}