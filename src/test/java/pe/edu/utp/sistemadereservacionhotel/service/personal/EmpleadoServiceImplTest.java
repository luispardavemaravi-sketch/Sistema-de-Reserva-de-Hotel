package pe.edu.utp.sistemadereservacionhotel.service.personal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.sistemadereservacionhotel.dto.personal.EmpleadoRequestDTO;
import pe.edu.utp.sistemadereservacionhotel.dto.personal.EmpleadoResponseDTO;
import pe.edu.utp.sistemadereservacionhotel.model.personal.CargoEmpleado;
import pe.edu.utp.sistemadereservacionhotel.model.personal.Empleado;
import pe.edu.utp.sistemadereservacionhotel.model.personal.Turno;
import pe.edu.utp.sistemadereservacionhotel.repository.personal.EmpleadoRepository;
import pe.edu.utp.sistemadereservacionhotel.repository.personal.TurnoRepository;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.RecursoNoEncontradoException;
import pe.edu.utp.sistemadereservacionhotel.service.personal.impl.EmpleadoServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static pe.edu.utp.sistemadereservacionhotel.model.personal.EspecialidadEmpleado.ADMINISTRACION;
import static pe.edu.utp.sistemadereservacionhotel.model.personal.EspecialidadEmpleado.ATENCION_AL_CLIENTE;

@ExtendWith(MockitoExtension.class)
class EmpleadoServiceImplTest {

    @Mock
    private EmpleadoRepository empleadoRepo;

    @Mock
    private TurnoRepository turnoRepo;

    @InjectMocks
    private EmpleadoServiceImpl empleadoService;

    @Test
    void registrarEmpleado_DatosValidos_OrquestaTurnoYGuarda() {
        // Arrange
        EmpleadoRequestDTO requestDTO = new EmpleadoRequestDTO(
                "Carlos", "Mendoza", "carlos.m@hotel.com", "987654321",
                CargoEmpleado.RECEPCIONISTA, ATENCION_AL_CLIENTE, "Av. Sol", "Lima", 2L
        );

        when(empleadoRepo.existsByEmail("carlos.m@hotel.com")).thenReturn(false);

        Turno turnoMock = new Turno();
        turnoMock.setIdTurno(2L);
        when(turnoRepo.findById(2L)).thenReturn(Optional.of(turnoMock));

        Empleado empleadoGuardado = new Empleado();
        empleadoGuardado.setIdEmpleado(10L);
        empleadoGuardado.setEmail("carlos.m@hotel.com");
        empleadoGuardado.setEstadoActivo(true);
        empleadoGuardado.setTurno(turnoMock);

        when(empleadoRepo.save(any(Empleado.class))).thenReturn(empleadoGuardado);

        // Act
        EmpleadoResponseDTO response = empleadoService.registrarEmpleado(requestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(2L, response.idTurno(), "El ID del turno no se asoció correctamente al empleado.");
        assertTrue(response.estadoActivo(), "El empleado debe crearse como activo por defecto.");

        verify(turnoRepo, times(1)).findById(2L);
        verify(empleadoRepo, times(1)).save(any(Empleado.class));
    }

    @Test
    void registrarEmpleado_TurnoInexistente_LanzaRecursoNoEncontradoException() {
        // Arrange
        EmpleadoRequestDTO requestDTO = new EmpleadoRequestDTO(
                "Maria", "Paz", "maria@hotel.com", "987654321",
                CargoEmpleado.GERENTE, ADMINISTRACION, "Av. Sol", "Lima", 99L // Turno fantasma
        );

        when(empleadoRepo.existsByEmail("maria@hotel.com")).thenReturn(false);
        when(turnoRepo.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecursoNoEncontradoException.class, () -> empleadoService.registrarEmpleado(requestDTO),
                "El sistema debe abortar la contratación si el turno asignado no existe.");

        verify(empleadoRepo, never()).save(any());
    }

    @Test
    void darDeBajaEmpleado_IdValido_AplicaBorradoLogico() {
        // Arrange
        Long idEmpleado = 15L;
        Empleado empleadoMock = new Empleado();
        empleadoMock.setIdEmpleado(idEmpleado);
        empleadoMock.setEstadoActivo(true);

        when(empleadoRepo.findById(idEmpleado)).thenReturn(Optional.of(empleadoMock));

        // Act
        empleadoService.darDeBajaEmpleado(idEmpleado);

        // Assert
        assertFalse(empleadoMock.getEstadoActivo(), "El borrado lógico no cambió el estado a falso.");
        verify(empleadoRepo, times(1)).save(empleadoMock);
        verify(empleadoRepo, never()).deleteById(anyLong()); // Garantía anti-DELETE SQL
    }
}