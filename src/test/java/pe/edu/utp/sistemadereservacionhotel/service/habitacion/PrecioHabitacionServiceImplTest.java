package pe.edu.utp.sistemadereservacionhotel.service.habitacion;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.sistemadereservacionhotel.dto.habitacion.PrecioHabitacionDTO;
import pe.edu.utp.sistemadereservacionhotel.model.habitacion.Habitacion;
import pe.edu.utp.sistemadereservacionhotel.model.habitacion.PrecioHabitacion;
import pe.edu.utp.sistemadereservacionhotel.repository.habitacion.HabitacionRepository;
import pe.edu.utp.sistemadereservacionhotel.repository.habitacion.PrecioHabitacionRepository;

import pe.edu.utp.sistemadereservacionhotel.service.habitacion.impl.PrecioHabitacionServiceImpl;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.ValidacionException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrecioHabitacionServiceImplTest {

    @Mock
    private PrecioHabitacionRepository precioRepo;

    @Mock
    private HabitacionRepository habitacionRepo;

    @InjectMocks
    private PrecioHabitacionServiceImpl precioService;

    @Test
    void establecerTarifa_DatosValidos_OrquestaGuardaMapea() {
        // Arrange: Fechas estáticas (Orden: idPrecio, idHabitacion, monto, inicio, fin)
        LocalDate inicio = LocalDate.of(2026, 1, 1);
        LocalDate fin = LocalDate.of(2026, 12, 31);
        PrecioHabitacionDTO requestDTO = new PrecioHabitacionDTO(null, 10L, new BigDecimal("250.00"), inicio, fin);

        Habitacion habitacionMock = new Habitacion();
        habitacionMock.setIdHabitacion(10L);
        when(habitacionRepo.findById(10L)).thenReturn(Optional.of(habitacionMock));

        PrecioHabitacion entidadGuardada = new PrecioHabitacion();
        entidadGuardada.setIdPrecio(50L);
        entidadGuardada.setMonto(new BigDecimal("250.00"));
        entidadGuardada.setFechaInicio(inicio);
        entidadGuardada.setFechaFin(fin);
        entidadGuardada.setHabitacion(habitacionMock);

        when(precioRepo.save(any(PrecioHabitacion.class))).thenReturn(entidadGuardada);

        // Act
        PrecioHabitacionDTO response = precioService.establecerTarifa(requestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(50L, response.id());
        assertEquals(new BigDecimal("250.00"), response.monto());
        verify(habitacionRepo, times(1)).findById(10L);
        verify(precioRepo, times(1)).save(any(PrecioHabitacion.class));
    }

    @Test
    void establecerTarifa_FechasInvertidas_LanzaValidacionException() {
        // Arrange: Fin ocurre antes que el inicio
        LocalDate inicio = LocalDate.of(2026, 12, 31);
        LocalDate fin = LocalDate.of(2026, 1, 1);
        PrecioHabitacionDTO requestDTO = new PrecioHabitacionDTO(null, 10L, new BigDecimal("250.00"), inicio, fin);

        // Act & Assert
        assertThrows(ValidacionException.class, () -> precioService.establecerTarifa(requestDTO),
                "Debe abortar la creación de tarifas con incoherencia temporal en RAM.");

        verify(habitacionRepo, never()).findById(any());
        verify(precioRepo, never()).save(any());
    }

    @Test
    void buscarPorRangoMonto_RangoInvertido_LanzaValidacionException() {
        // Arrange: Min mayor que Max
        BigDecimal min = new BigDecimal("500.00");
        BigDecimal max = new BigDecimal("100.00");

        // Act & Assert
        assertThrows(ValidacionException.class, () -> precioService.buscarPorRangoMonto(min, max),
                "Debe bloquear búsquedas financieras mal formuladas.");

        verify(precioRepo, never()).findByMontoBetween(any(), any());
    }
}