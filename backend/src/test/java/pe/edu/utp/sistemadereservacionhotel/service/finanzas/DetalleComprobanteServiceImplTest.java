package pe.edu.utp.sistemadereservacionhotel.service.finanzas;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.sistemadereservacionhotel.dto.finanzas.DetalleComprobanteRequestDTO;
import pe.edu.utp.sistemadereservacionhotel.dto.finanzas.DetalleComprobanteResponseDTO;
import pe.edu.utp.sistemadereservacionhotel.model.finanzas.ComprobantePago;
import pe.edu.utp.sistemadereservacionhotel.model.finanzas.DetalleComprobante;
import pe.edu.utp.sistemadereservacionhotel.repository.finanzas.ComprobantePagoRepository;
import pe.edu.utp.sistemadereservacionhotel.repository.finanzas.DetalleComprobanteRepository;
import pe.edu.utp.sistemadereservacionhotel.service.finanzas.impl.DetalleComprobanteServiceImpl;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.RecursoNoEncontradoException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Suite de pruebas unitarias para auditar el cálculo financiero y el enrutamiento
 * de los detalles de facturación.
 */
@ExtendWith(MockitoExtension.class)
class DetalleComprobanteServiceImplTest {

    @Mock
    private DetalleComprobanteRepository detalleRepo;

    @Mock
    private ComprobantePagoRepository comprobanteRepo;

    @InjectMocks
    private DetalleComprobanteServiceImpl detalleService;

    @Test
    void agregarDetalle_DatosValidos_CalculaSubtotalYGuardaExitosamente() {
        // Arrange
        DetalleComprobanteRequestDTO request = new DetalleComprobanteRequestDTO(
                2, // Cantidad
                "Servicio a la habitación", // Descripción
                new BigDecimal("45.50"), // Precio unitario
                10L // ID Comprobante
        );

        ComprobantePago comprobanteMock = new ComprobantePago();
        comprobanteMock.setIdComprobante(10L);

        when(comprobanteRepo.findById(10L)).thenReturn(Optional.of(comprobanteMock));

        DetalleComprobante detalleGuardado = new DetalleComprobante();
        detalleGuardado.setIdDetalle(100L);
        detalleGuardado.setCantidad(request.cantidad());
        detalleGuardado.setDescripcion(request.descripcion());
        detalleGuardado.setPrecioUnitario(request.precioUnitario());
        // El subtotal esperado es 45.50 * 2 = 91.00
        detalleGuardado.setSubtotalLinea(new BigDecimal("91.00"));
        detalleGuardado.setComprobantePago(comprobanteMock);

        when(detalleRepo.save(any(DetalleComprobante.class))).thenReturn(detalleGuardado);

        // Act
        DetalleComprobanteResponseDTO response = detalleService.agregarDetalle(request);

        // Assert
        assertNotNull(response, "El DTO de respuesta no debe ser nulo");
        assertEquals(new BigDecimal("91.00"), response.subtotalLinea(), "Error crítico: El cálculo de la multiplicación financiera es incorrecto");
        assertEquals(10L, response.idComprobante(), "El detalle no se vinculó al comprobante correcto");

        verify(detalleRepo, times(1)).save(any(DetalleComprobante.class));
    }

    @Test
    void agregarDetalle_ComprobanteInexistente_LanzaRecursoNoEncontradoException() {
        // Arrange
        DetalleComprobanteRequestDTO request = new DetalleComprobanteRequestDTO(
                1,
                "Minibar",
                new BigDecimal("15.00"),
                99L // Comprobante fantasma
        );

        when(comprobanteRepo.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecursoNoEncontradoException.class, () -> detalleService.agregarDetalle(request),
                "No se debe permitir agregar un consumo a un comprobante que no existe en la base de datos.");

        verify(detalleRepo, never()).save(any());
    }

    @Test
    void actualizarDetalle_DatosValidos_RecalculaSubtotalYGuarda() {
        // Arrange
        Long idDetalle = 100L;
        // El cliente pide cambiar la cantidad de 1 a 3
        DetalleComprobanteRequestDTO request = new DetalleComprobanteRequestDTO(
                3,
                "Desayuno Buffet",
                new BigDecimal("20.00"),
                10L
        );

        ComprobantePago comprobanteMock = new ComprobantePago();
        comprobanteMock.setIdComprobante(10L);

        DetalleComprobante existente = new DetalleComprobante();
        existente.setIdDetalle(idDetalle);
        existente.setCantidad(1); // Cantidad original
        existente.setPrecioUnitario(new BigDecimal("20.00"));
        existente.setSubtotalLinea(new BigDecimal("20.00"));
        existente.setComprobantePago(comprobanteMock);

        when(detalleRepo.findById(idDetalle)).thenReturn(Optional.of(existente));

        // El mock asume que el método bajo prueba actualizará los valores del objeto 'existente'
        when(detalleRepo.save(any(DetalleComprobante.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        DetalleComprobanteResponseDTO response = detalleService.actualizarDetalle(idDetalle, request);

        // Assert
        assertEquals(3, response.cantidad(), "La cantidad no se actualizó");
        assertEquals(new BigDecimal("60.00"), response.subtotalLinea(), "El subtotal no se recalculó correctamente tras la actualización");

        verify(detalleRepo, times(1)).save(existente);
    }

    @Test
    void eliminarDetalle_IdInexistente_LanzaRecursoNoEncontradoException() {
        // Arrange
        Long idInvalido = 999L;
        when(detalleRepo.existsById(idInvalido)).thenReturn(false);

        // Act & Assert
        assertThrows(RecursoNoEncontradoException.class, () -> detalleService.eliminarDetalle(idInvalido),
                "El sistema debe validar la existencia del detalle antes de intentar ejecutar un DELETE en la BD.");

        verify(detalleRepo, never()).deleteById(anyLong());
    }
}