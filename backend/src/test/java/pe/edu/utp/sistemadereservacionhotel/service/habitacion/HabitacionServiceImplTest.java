package pe.edu.utp.sistemadereservacionhotel.service.habitacion;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.sistemadereservacionhotel.dto.habitacion.HabitacionRequestDTO;
import pe.edu.utp.sistemadereservacionhotel.dto.habitacion.HabitacionResponseDTO;
import pe.edu.utp.sistemadereservacionhotel.model.habitacion.Habitacion;
import pe.edu.utp.sistemadereservacionhotel.model.habitacion.Piso;
import pe.edu.utp.sistemadereservacionhotel.model.habitacion.TipoHabitacion;
import pe.edu.utp.sistemadereservacionhotel.repository.habitacion.HabitacionRepository;
import pe.edu.utp.sistemadereservacionhotel.repository.habitacion.PisoRepository;
import pe.edu.utp.sistemadereservacionhotel.repository.habitacion.TipoHabitacionRepository;
import pe.edu.utp.sistemadereservacionhotel.service.habitacion.impl.HabitacionServiceImpl;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.ValidacionException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HabitacionServiceImplTest {

    @Mock
    private HabitacionRepository habitacionRepo;

    @Mock
    private PisoRepository pisoRepo;

    @Mock
    private TipoHabitacionRepository tipoHabitacionRepo;

    @InjectMocks
    private HabitacionServiceImpl habitacionService;

    @Test
    void registrarHabitacion_DatosValidos_OrquestaRepositoriosYGuarda() {
        // Arrange: Orden estricto según HabitacionRequestDTO -> (String, Long, Long, BigDecimal)
        HabitacionRequestDTO requestDTO = new HabitacionRequestDTO("201", 2L, 3L, new BigDecimal("150.00"));

        when(habitacionRepo.existsByNumeroHabitacion("201")).thenReturn(false);

        Piso pisoMock = new Piso();
        pisoMock.setIdPiso(2L);
        when(pisoRepo.findById(2L)).thenReturn(Optional.of(pisoMock));

        TipoHabitacion tipoMock = new TipoHabitacion();

        tipoMock.setIdTipo(3L);
        tipoMock.setNombre("MATRIMONIAL");
        when(tipoHabitacionRepo.findById(3L)).thenReturn(Optional.of(tipoMock));

        Habitacion entidadGuardada = new Habitacion();
        entidadGuardada.setIdHabitacion(10L);
        entidadGuardada.setNumeroHabitacion("201");
        entidadGuardada.setPrecioActual(new BigDecimal("150.00"));
        entidadGuardada.setEstadoActivo(true);
        entidadGuardada.setTipoHabitacion(tipoMock);

        when(habitacionRepo.save(any(Habitacion.class))).thenReturn(entidadGuardada);

        // Act
        HabitacionResponseDTO response = habitacionService.registrarHabitacion(requestDTO);

        // Assert: Usamos response.activo() porque HabitacionResponseDTO es un Record
        assertNotNull(response);
        assertTrue(response.activo(), "La habitación debe nacer con estado activo por defecto.");
        assertEquals("MATRIMONIAL", response.tipoHabitacion(), "Fallo en el mapeo transversal de la relación.");

        verify(pisoRepo, times(1)).findById(2L);
        verify(tipoHabitacionRepo, times(1)).findById(3L);
        verify(habitacionRepo, times(1)).save(any(Habitacion.class));
    }

    @Test
    void darDeBajaHabitacion_IdValido_RealizaBorradoLogicoSinEjecutarDelete() {
        // Arrange
        Long idHabitacion = 5L;
        Habitacion habitacionExistente = new Habitacion();
        habitacionExistente.setIdHabitacion(idHabitacion);
        habitacionExistente.setEstadoActivo(true);

        when(habitacionRepo.findById(idHabitacion)).thenReturn(Optional.of(habitacionExistente));

        // Act
        habitacionService.darDeBajaHabitacion(idHabitacion);

        // Assert
        assertFalse(habitacionExistente.isEstadoActivo(), "El estado lógico no cambió a inactivo.");
        verify(habitacionRepo, times(1)).save(habitacionExistente);
        verify(habitacionRepo, never()).deleteById(anyLong());
    }

    @Test
    void buscarPorRangoPrecio_RangoInvalido_LanzaValidacionException() {
        // Arrange
        BigDecimal min = new BigDecimal("500.00");
        BigDecimal max = new BigDecimal("100.00");

        // Act & Assert
        assertThrows(ValidacionException.class, () -> habitacionService.buscarPorRangoPrecio(min, max),
                "Debe bloquear la consulta si el rango de precios está invertido.");

        verify(habitacionRepo, never()).findByPrecioActualBetween(any(), any());
    }

    @Test
    void actualizarTarifa_PrecioNegativo_LanzaValidacionException() {
        // Arrange
        BigDecimal precioInvalido = new BigDecimal("-50.00");

        // Act & Assert
        assertThrows(ValidacionException.class, () -> habitacionService.actualizarTarifa(1L, precioInvalido),
                "El sistema financiero de habitaciones debe rechazar tarifas negativas.");

        verify(habitacionRepo, never()).findById(any());
        verify(habitacionRepo, never()).save(any());
    }
}