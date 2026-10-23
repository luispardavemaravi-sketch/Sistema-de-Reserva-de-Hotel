package pe.edu.utp.sistemadereservacionhotel.service.personal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.sistemadereservacionhotel.dto.personal.TurnoDTO;
import pe.edu.utp.sistemadereservacionhotel.model.personal.Turno;
import pe.edu.utp.sistemadereservacionhotel.repository.personal.TurnoRepository;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.ValidacionException;
import pe.edu.utp.sistemadereservacionhotel.service.personal.impl.TurnoServiceImpl;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TurnoServiceImplTest {

    @Mock
    private TurnoRepository turnoRepo;

    @InjectMocks
    private TurnoServiceImpl turnoService;

    @Test
    void registrarTurno_HorarioCoherente_GuardaExitosamente() {
        // Arrange
        LocalTime inicio = LocalTime.of(8, 0); // 08:00 AM
        LocalTime fin = LocalTime.of(16, 0);   // 04:00 PM
        TurnoDTO requestDTO = new TurnoDTO(null, inicio, fin, Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY));

        Turno turnoGuardado = new Turno();
        turnoGuardado.setIdTurno(1L);
        turnoGuardado.setHoraInicio(inicio);
        turnoGuardado.setHoraFinal(fin);
        turnoGuardado.setDiasSemana(Set.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY));

        when(turnoRepo.save(any(Turno.class))).thenReturn(turnoGuardado);

        // Act
        TurnoDTO response = turnoService.registrarTurno(requestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(inicio, response.horaInicio());
        assertEquals(fin, response.horaFinal());
        assertTrue(response.diasSemana().contains(DayOfWeek.MONDAY));

        verify(turnoRepo, times(1)).save(any(Turno.class));
    }

    @Test
    void registrarTurno_HorarioInvertido_LanzaValidacionException() {
        // Arrange: Empleado no puede salir antes de entrar
        LocalTime inicio = LocalTime.of(18, 0); // 06:00 PM
        LocalTime fin = LocalTime.of(10, 0);    // 10:00 AM
        TurnoDTO requestDTO = new TurnoDTO(null, inicio, fin, Set.of(DayOfWeek.FRIDAY));

        // Act & Assert
        assertThrows(ValidacionException.class, () -> turnoService.registrarTurno(requestDTO),
                "El sistema debe bloquear la creación de turnos con paradojas temporales.");

        verify(turnoRepo, never()).save(any());
    }

    @Test
    void buscarPorDia_ParametroNulo_LanzaValidacionExceptionFailFast() {
        // Act & Assert
        assertThrows(ValidacionException.class, () -> turnoService.buscarPorDia(null),
                "Búsquedas con parámetros nulos deben abortarse antes de tocar el repositorio.");

        verify(turnoRepo, never()).findByDiasSemana(any());
    }
}