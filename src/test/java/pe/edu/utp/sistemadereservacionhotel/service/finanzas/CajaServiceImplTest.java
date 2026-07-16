package pe.edu.utp.sistemadereservacionhotel.service.finanzas;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.sistemadereservacionhotel.dto.finanzas.CajaAperturaDTO;
import pe.edu.utp.sistemadereservacionhotel.dto.finanzas.CajaResponseDTO;
import pe.edu.utp.sistemadereservacionhotel.model.finanzas.Caja;
import pe.edu.utp.sistemadereservacionhotel.model.personal.Empleado;
import pe.edu.utp.sistemadereservacionhotel.repository.finanzas.CajaRepository;
import pe.edu.utp.sistemadereservacionhotel.repository.personal.EmpleadoRepository;
import pe.edu.utp.sistemadereservacionhotel.service.finanzas.impl.CajaServiceImpl;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.DuplicadoException;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.ValidacionException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Suite de pruebas unitarias orientada a la validación de estados y reglas de negocio financieras.
 */
@ExtendWith(MockitoExtension.class)
class CajaServiceImplTest {

    @Mock
    private CajaRepository cajaRepo;

    @Mock
    private EmpleadoRepository empleadoRepo;

    @InjectMocks
    private CajaServiceImpl cajaService;

    @Test
    void abrirCaja_DatosValidos_AbreCajaExitosamente() {
        // Arrange
        CajaAperturaDTO dto = new CajaAperturaDTO(1L, new BigDecimal("100.00"));

        Empleado empleadoMock = new Empleado();
        empleadoMock.setIdEmpleado(1L);

        when(empleadoRepo.findById(1L)).thenReturn(Optional.of(empleadoMock));
        when(cajaRepo.findByEmpleado_IdEmpleado(1L)).thenReturn(List.of());

        Caja cajaGuardada = new Caja();
        cajaGuardada.setIdCaja(10L);
        cajaGuardada.setMontoApertura(new BigDecimal("100.00"));
        cajaGuardada.setEstaAbierta(true);
        cajaGuardada.setEmpleado(empleadoMock);

        when(cajaRepo.save(any(Caja.class))).thenReturn(cajaGuardada);

        // Act
        CajaResponseDTO response = cajaService.abrirCaja(dto);

        // Assert
        assertNotNull(response, "El DTO no debe ser nulo");
        assertTrue(response.estaAbierta(), "La caja mapeada debe figurar como abierta");
        verify(cajaRepo, times(1)).save(any(Caja.class));
    }

    @Test
    void abrirCaja_EmpleadoConCajaYaAbierta_LanzaDuplicadoException() {
        // Arrange
        CajaAperturaDTO dto = new CajaAperturaDTO(1L, new BigDecimal("50.00"));

        Empleado empleadoMock = new Empleado();
        empleadoMock.setIdEmpleado(1L);

        Caja cajaAbiertaExistente = new Caja();
        cajaAbiertaExistente.setEstaAbierta(true);

        when(empleadoRepo.findById(1L)).thenReturn(Optional.of(empleadoMock));
        when(cajaRepo.findByEmpleado_IdEmpleado(1L)).thenReturn(List.of(cajaAbiertaExistente));

        // Act & Assert (Lambda limpia)
        assertThrows(DuplicadoException.class, () -> cajaService.abrirCaja(dto),
                "El sistema debe bloquear la apertura si el empleado ya posee una sesión activa.");

        verify(cajaRepo, never()).save(any());
    }

    @Test
    void cerrarCaja_MontoValidoYCajaAbierta_CierraCajaExitosamente() {
        // Arrange
        Long idCaja = 10L;
        BigDecimal montoCierre = new BigDecimal("500.00");

        Empleado empleadoMock = new Empleado();
        empleadoMock.setIdEmpleado(1L);

        Caja cajaMock = new Caja();
        cajaMock.setIdCaja(idCaja);
        cajaMock.setEstaAbierta(true);
        cajaMock.setEmpleado(empleadoMock);

        when(cajaRepo.findById(idCaja)).thenReturn(Optional.of(cajaMock));
        when(cajaRepo.save(any(Caja.class))).thenReturn(cajaMock);

        // Act
        CajaResponseDTO response = cajaService.cerrarCaja(idCaja, montoCierre);

        // Assert
        assertFalse(response.estaAbierta(), "El estado de la caja debe cambiar a cerrado.");
        assertEquals(montoCierre, response.montoCierre(), "El monto de cierre no coincide.");
        verify(cajaRepo, times(1)).save(cajaMock);
    }

    @Test
    void cerrarCaja_MontoNegativo_LanzaValidacionExceptionPorFailFast() {
        // Arrange
        BigDecimal montoInvalido = new BigDecimal("-10.00");

        // Act & Assert (Lambda limpia, sin asignación inútil)
        assertThrows(ValidacionException.class, () -> cajaService.cerrarCaja(1L, montoInvalido),
                "Se debe rechazar un arqueo con monto negativo inmediatamente.");

        verify(cajaRepo, never()).findById(any());
        verify(cajaRepo, never()).save(any());
    }

    @Test
    void cerrarCaja_CajaYaCerrada_LanzaValidacionException() {
        // Arrange
        Long idCaja = 10L;
        BigDecimal montoCierre = new BigDecimal("100.00");

        Caja cajaCerrada = new Caja();
        cajaCerrada.setIdCaja(idCaja);
        cajaCerrada.setEstaAbierta(false);

        when(cajaRepo.findById(idCaja)).thenReturn(Optional.of(cajaCerrada));

        // Act & Assert (Lambda limpia, variable montoCierre extraída al Arrange)
        assertThrows(ValidacionException.class, () -> cajaService.cerrarCaja(idCaja, montoCierre),
                "No se puede cerrar una sesión que ya ha sido clausurada.");

        verify(cajaRepo, never()).save(any());
    }

    @Test
    void buscarPorRangoFecha_FechasInvertidas_LanzaValidacionException() {
        // Arrange (Fechas estáticas, no atadas al reloj del sistema)
        LocalDate fechaInicio = LocalDate.of(2026, 12, 31);
        LocalDate fechaFin = LocalDate.of(2026, 1, 1);

        // Act & Assert (Lambda limpia)
        assertThrows(ValidacionException.class, () -> cajaService.buscarPorRangoFecha(fechaInicio, fechaFin),
                "Debe validar la coherencia temporal antes de consultar la base de datos.");

        verify(cajaRepo, never()).findByFechaBetween(any(), any());
    }
}