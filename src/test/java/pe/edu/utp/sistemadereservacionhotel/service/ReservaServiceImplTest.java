package pe.edu.utp.sistemadereservacionhotel.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.sistemadereservacionhotel.dto.reserva.ReservaRequestDTO;
import pe.edu.utp.sistemadereservacionhotel.dto.reserva.ReservaResponseDTO;
import pe.edu.utp.sistemadereservacionhotel.model.habitacion.Habitacion;
import pe.edu.utp.sistemadereservacionhotel.model.huesped.Huesped;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.EstadoReserva;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.Reserva;
import pe.edu.utp.sistemadereservacionhotel.repository.reserva.EstadoReservaRepository;
import pe.edu.utp.sistemadereservacionhotel.repository.reserva.ReservaRepository;
import pe.edu.utp.sistemadereservacionhotel.repository.habitacion.HabitacionRepository;
import pe.edu.utp.sistemadereservacionhotel.repository.huesped.HuespedRepository;
import pe.edu.utp.sistemadereservacionhotel.service.reserva.impl.ReservaServiceImpl;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Suite de pruebas unitarias para aislar y validar la lógica de negocio de ReservaServiceImpl.
 * Utiliza Mockito para simular la capa de persistencia (JPA/Hibernate) y garantizar
 * que las pruebas no dependan de conexiones a bases de datos ni de tiempos de red.
 */
@ExtendWith(MockitoExtension.class)
class ReservaServiceImplTest {

    @Mock
    private ReservaRepository reservaRepo;

    @Mock
    private HabitacionRepository habitacionRepo;

    @Mock
    private HuespedRepository huespedRepo;

    @Mock
    private EstadoReservaRepository estadoRepo;

    @InjectMocks
    private ReservaServiceImpl reservaService;

    /**
     * Prueba el escenario de éxito ("Happy Path") en la creación de una reserva.
     * Verifica que si los datos de entrada son válidos y las dependencias existen en BD,
     * el servicio orquesta correctamente las llamadas a los repositorios y retorna un DTO válido.
     */
    @Test
    void crearReserva_DatosValidos_RetornaReservaDTONuevo() {
        // 1. Arrange (Configuración del estado inicial y Mocks)
        ReservaRequestDTO requestDTO = new ReservaRequestDTO(
                1L,
                2L,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(5)
        );

        Huesped huespedMock = new Huesped();
        huespedMock.setIdHuesped(1L);
        when(huespedRepo.findById(1L)).thenReturn(Optional.of(huespedMock));

        Habitacion habitacionMock = new Habitacion();
        habitacionMock.setIdHabitacion(2L);
        when(habitacionRepo.findById(2L)).thenReturn(Optional.of(habitacionMock));

        EstadoReserva estadoMock = new EstadoReserva();
        estadoMock.setIdEstado(1L);
        when(estadoRepo.findById(1L)).thenReturn(Optional.of(estadoMock));

        Reserva reservaGuardada = new Reserva();
        reservaGuardada.setIdReserva(100L);
        reservaGuardada.setCodigoReserva("RES-TEST-01");
        reservaGuardada.setHuesped(huespedMock);
        reservaGuardada.setHabitacion(habitacionMock);
        reservaGuardada.setEstadoReserva(estadoMock);
        when(reservaRepo.save(any(Reserva.class))).thenReturn(reservaGuardada);

        // 2. Act (Ejecución del método a probar)
        ReservaResponseDTO response = reservaService.crearReserva(requestDTO);

        // 3. Assert (Verificación de resultados y comportamiento)
        assertNotNull(response, "El DTO de respuesta no debe ser nulo tras una inserción exitosa.");
        assertEquals("RES-TEST-01", response.codigoReserva(), "El código de reserva mapeado en el DTO es incorrecto.");

        // Verificación crítica de I/O: Asegura que el método save() se llamó exactamente una vez.
        verify(reservaRepo, times(1)).save(any(Reserva.class));
    }

    /**
     * Prueba el principio de diseño "Fail-Fast".
     * Válida que una petición con incoherencia temporal (salida anterior a entrada)
     * sea rechazada en memoria RAM, abortando el proceso antes de consumir recursos de I/O (Base de Datos).
     */
    @Test
    void crearReserva_FechasIncoherentes_LanzaExcepcionFailFast() {
        // Arrange
        ReservaRequestDTO requestInvalido = new ReservaRequestDTO(
                1L,
                2L,
                LocalDate.now().plusDays(5), // Entrada
                LocalDate.now().plusDays(1)  // Salida (Incoherencia temporal)
        );

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            reservaService.crearReserva(requestInvalido);
        }, "El sistema debe lanzar IllegalArgumentException al detectar fechas invertidas.");

        assertTrue(exception.getMessage().contains("fecha"), "El mensaje de la excepción carece de contexto técnico sobre el fallo de fechas.");

        // Verificación de aislamiento (QA): Garantiza que los repositorios nunca fueron invocados.
        verify(huespedRepo, never()).findById(any());
        verify(habitacionRepo, never()).findById(any());
        verify(reservaRepo, never()).save(any());
    }
}