package pe.edu.utp.sistemadereservacionhotel.service.finanzas;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.sistemadereservacionhotel.dto.finanzas.ComprobanteRequestDTO;
import pe.edu.utp.sistemadereservacionhotel.dto.finanzas.ComprobanteResponseDTO;
import pe.edu.utp.sistemadereservacionhotel.model.finanzas.ComprobantePago;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.Reserva;
import pe.edu.utp.sistemadereservacionhotel.repository.finanzas.ComprobantePagoRepository;
import pe.edu.utp.sistemadereservacionhotel.repository.reserva.ReservaRepository;
import pe.edu.utp.sistemadereservacionhotel.service.finanzas.impl.ComprobantePagoServiceImpl;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.DuplicadoException;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.RecursoNoEncontradoException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Suite de pruebas unitarias para aislar y auditar el servicio de facturación.
 * Evalúa las reglas anti-duplicidad y la correcta delegación al patrón Factory.
 */
@ExtendWith(MockitoExtension.class)
class ComprobantePagoServiceImplTest {

    @Mock
    private ComprobantePagoRepository comprobanteRepo;

    @Mock
    private ReservaRepository reservaRepo;

    @InjectMocks
    private ComprobantePagoServiceImpl comprobanteService;

    @Test
    void emitirComprobante_ReservaValidaYNoFacturada_EmiteExitosamente() {
        // Arrange
        ComprobanteRequestDTO requestDTO = new ComprobanteRequestDTO(1L, "FACTURA");

        Reserva reservaMock = new Reserva();
        reservaMock.setIdReserva(1L);
        reservaMock.setMontoTotalEstimado(new BigDecimal("200.00")); // Crítico para que el Factory no arroje NullPointerException

        when(reservaRepo.findById(1L)).thenReturn(Optional.of(reservaMock));
        // Simulamos que la reserva aún no tiene comprobantes emitidos
        when(comprobanteRepo.findByReserva_IdReserva(1L)).thenReturn(Optional.empty());

        ComprobantePago comprobanteGuardado = new ComprobantePago();
        comprobanteGuardado.setIdComprobante(50L);
        comprobanteGuardado.setNumeroSerie("F001-TEST");
        comprobanteGuardado.setMontoTotal(new BigDecimal("236.00")); // 200 + 18% IGV
        comprobanteGuardado.setFechaEmision(LocalDateTime.of(2026, 10, 15, 14, 30));
        comprobanteGuardado.setReserva(reservaMock);

        when(comprobanteRepo.save(any(ComprobantePago.class))).thenReturn(comprobanteGuardado);

        // Act
        ComprobanteResponseDTO response = comprobanteService.emitirComprobante(requestDTO);

        // Assert
        assertNotNull(response, "El comprobante emitido no debe ser nulo.");
        assertEquals("F001-TEST", response.numeroSerie(), "El número de serie no se mapeó correctamente.");
        assertEquals(new BigDecimal("236.00"), response.montoTotal(), "El monto total mapeado es incorrecto.");

        verify(comprobanteRepo, times(1)).save(any(ComprobantePago.class));
    }

    @Test
    void emitirComprobante_ReservaYaFacturada_LanzaDuplicadoException() {
        // Arrange
        ComprobanteRequestDTO requestDTO = new ComprobanteRequestDTO(1L, "BOLETA");

        Reserva reservaMock = new Reserva();
        reservaMock.setIdReserva(1L);

        ComprobantePago comprobanteExistente = new ComprobantePago();
        comprobanteExistente.setIdComprobante(10L);

        when(reservaRepo.findById(1L)).thenReturn(Optional.of(reservaMock));
        // Simulamos colisión: el repositorio ya encuentra un comprobante para esta reserva
        when(comprobanteRepo.findByReserva_IdReserva(1L)).thenReturn(Optional.of(comprobanteExistente));

        // Act & Assert (Lambda limpia en una sola línea)
        assertThrows(DuplicadoException.class, () -> comprobanteService.emitirComprobante(requestDTO),
                "El sistema debe bloquear la emisión de múltiples comprobantes para una misma reserva.");

        verify(comprobanteRepo, never()).save(any(ComprobantePago.class));
    }

    @Test
    void emitirComprobante_ReservaInexistente_LanzaRecursoNoEncontradoException() {
        // Arrange
        ComprobanteRequestDTO requestDTO = new ComprobanteRequestDTO(99L, "FACTURA");

        when(reservaRepo.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecursoNoEncontradoException.class, () -> comprobanteService.emitirComprobante(requestDTO),
                "No se puede emitir un comprobante sobre una reserva fantasma.");

        verify(comprobanteRepo, never()).findByReserva_IdReserva(any());
        verify(comprobanteRepo, never()).save(any());
    }

    @Test
    void anularComprobante_IdInexistente_LanzaRecursoNoEncontradoException() {
        // Arrange
        Long idInvalido = 999L;
        when(comprobanteRepo.findById(idInvalido)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecursoNoEncontradoException.class, () -> comprobanteService.anularComprobante(idInvalido),
                "Debe fallar al intentar anular un documento legal inexistente.");
    }
}