package pe.edu.utp.sistemadereservacionhotel.service.habitacion;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.sistemadereservacionhotel.dto.habitacion.PisoDTO;
import pe.edu.utp.sistemadereservacionhotel.model.habitacion.Piso;
import pe.edu.utp.sistemadereservacionhotel.repository.habitacion.PisoRepository;
import pe.edu.utp.sistemadereservacionhotel.service.habitacion.impl.PisoServiceImpl;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.DuplicadoException;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.RecursoNoEncontradoException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PisoServiceImplTest {

    @Mock
    private PisoRepository pisoRepo;

    @InjectMocks
    private PisoServiceImpl pisoService;

    @Test
    void registrarPiso_DatosValidos_GuardaExitosamente() {
        // Arrange (Orden: id, numeroPiso, sector)
        PisoDTO requestDTO = new PisoDTO(null, 5, "Torre Norte");
        when(pisoRepo.existsByNumeroPiso(5)).thenReturn(false);

        Piso pisoGuardado = new Piso();
        pisoGuardado.setIdPiso(1L);
        pisoGuardado.setNumeroPiso(5);
        pisoGuardado.setSector("Torre Norte");

        when(pisoRepo.save(any(Piso.class))).thenReturn(pisoGuardado);

        // Act
        PisoDTO response = pisoService.registrarPiso(requestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals(5, response.numeroPiso());
        verify(pisoRepo, times(1)).save(any(Piso.class));
    }

    @Test
    void registrarPiso_NumeroDuplicado_LanzaDuplicadoException() {
        // Arrange
        PisoDTO requestDTO = new PisoDTO(null, 3, "Torre Sur");
        when(pisoRepo.existsByNumeroPiso(3)).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicadoException.class, () -> pisoService.registrarPiso(requestDTO),
                "El sistema debe bloquear el registro de pisos con el mismo número.");

        verify(pisoRepo, never()).save(any());
    }

    @Test
    void actualizarPiso_PisoInexistente_LanzaRecursoNoEncontradoException() {
        // Arrange
        PisoDTO requestDTO = new PisoDTO(99L, 10, "Penthouse");
        when(pisoRepo.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecursoNoEncontradoException.class, () -> pisoService.actualizarPiso(99L, requestDTO),
                "Debe fallar al intentar actualizar un piso que no existe.");

        verify(pisoRepo, never()).save(any());
    }
}