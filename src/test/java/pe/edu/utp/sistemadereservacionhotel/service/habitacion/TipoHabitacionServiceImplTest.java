package pe.edu.utp.sistemadereservacionhotel.service.habitacion;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.sistemadereservacionhotel.dto.habitacion.TipoHabitacionDTO;
import pe.edu.utp.sistemadereservacionhotel.model.habitacion.TipoHabitacion;
import pe.edu.utp.sistemadereservacionhotel.repository.habitacion.TipoHabitacionRepository;
import pe.edu.utp.sistemadereservacionhotel.service.habitacion.impl.TipoHabitacionServiceImpl;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.DuplicadoException;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.ValidacionException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TipoHabitacionServiceImplTest {

    @Mock
    private TipoHabitacionRepository tipoHabitacionRepo;

    @InjectMocks
    private TipoHabitacionServiceImpl tipoHabitacionService;

    @Test
    void registrarTipo_DatosValidos_GuardaExitosamente() {
        // Arrange (Orden: idTipo, nombre, capacidadMaxima, precioBase)
        TipoHabitacionDTO requestDTO = new TipoHabitacionDTO(null, "SUITE PRESIDENCIAL", 4, new BigDecimal("1000.00"));
        when(tipoHabitacionRepo.existsByNombre("SUITE PRESIDENCIAL")).thenReturn(false);

        TipoHabitacion tipoGuardado = new TipoHabitacion();
        tipoGuardado.setIdTipo(5L);
        tipoGuardado.setNombre("SUITE PRESIDENCIAL");
        tipoGuardado.setCapacidadMaxima(4);
        tipoGuardado.setPrecioBase(new BigDecimal("1000.00"));

        when(tipoHabitacionRepo.save(any(TipoHabitacion.class))).thenReturn(tipoGuardado);

        // Act
        TipoHabitacionDTO response = tipoHabitacionService.registrarTipo(requestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(5L, response.id());
        assertEquals("SUITE PRESIDENCIAL", response.nombre());
        verify(tipoHabitacionRepo, times(1)).save(any(TipoHabitacion.class));
    }

    @Test
    void registrarTipo_NombreDuplicado_LanzaDuplicadoException() {
        // Arrange
        TipoHabitacionDTO requestDTO = new TipoHabitacionDTO(null, "DOBLE", 2, new BigDecimal("150.00"));
        when(tipoHabitacionRepo.existsByNombre("DOBLE")).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicadoException.class, () -> tipoHabitacionService.registrarTipo(requestDTO),
                "Debe bloquear el registro de categorías con nomenclatura repetida.");

        verify(tipoHabitacionRepo, never()).save(any());
    }

    @Test
    void buscarPorRangoPrecio_RangoInvalido_LanzaValidacionException() {
        // Arrange
        BigDecimal min = new BigDecimal("800.00");
        BigDecimal max = new BigDecimal("200.00");

        // Act & Assert
        assertThrows(ValidacionException.class, () -> tipoHabitacionService.buscarPorRangoPrecio(min, max),
                "El sistema debe proteger la base de datos de consultas mal formadas.");

        verify(tipoHabitacionRepo, never()).findByPrecioBaseBetween(any(), any());
    }
}