package pe.edu.utp.sistemadereservacionhotel.service.habitacion;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.sistemadereservacionhotel.dto.habitacion.EstadoHabitacionDTO;

import pe.edu.utp.sistemadereservacionhotel.repository.habitacion.EstadoHabitacionRepository;
import pe.edu.utp.sistemadereservacionhotel.service.habitacion.impl.EstadoHabitacionServiceImpl;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.RecursoNoEncontradoException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstadoHabitacionServiceImplTest {

    @Mock
    private EstadoHabitacionRepository estadoRepo;

    @InjectMocks
    private EstadoHabitacionServiceImpl estadoService;

    @Test
    void registrar_NombreDuplicado_LanzaIllegalArgumentException() {
        // Arrange
        EstadoHabitacionDTO requestDTO = new EstadoHabitacionDTO(null, "MANTENIMIENTO");
        when(estadoRepo.existsByNombreEstado("MANTENIMIENTO")).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> estadoService.registrar(requestDTO),
                "No se deben permitir estados con nomenclatura duplicada.");

        verify(estadoRepo, never()).save(any());
    }

    @Test
    void actualizar_IdInexistente_LanzaRecursoNoEncontradoException() {
        // Arrange
        EstadoHabitacionDTO requestDTO = new EstadoHabitacionDTO(99L, "LIMPIEZA");
        when(estadoRepo.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecursoNoEncontradoException.class, () -> estadoService.actualizar(99L, requestDTO),
                "Debe fallar al intentar actualizar un estado que no existe en BD.");

        verify(estadoRepo, never()).save(any());
    }
}