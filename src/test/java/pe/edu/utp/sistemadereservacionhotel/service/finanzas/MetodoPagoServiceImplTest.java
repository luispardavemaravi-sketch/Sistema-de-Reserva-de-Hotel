package pe.edu.utp.sistemadereservacionhotel.service.finanzas;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.sistemadereservacionhotel.dto.finanzas.MetodoPagoDTO;
import pe.edu.utp.sistemadereservacionhotel.model.finanzas.MetodoPago;
import pe.edu.utp.sistemadereservacionhotel.repository.finanzas.MetodoPagoRepository;
import pe.edu.utp.sistemadereservacionhotel.service.finanzas.impl.MetodoPagoServiceImpl;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.DuplicadoException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MetodoPagoServiceImplTest {

    @Mock
    private MetodoPagoRepository metodoPagoRepo;

    @InjectMocks
    private MetodoPagoServiceImpl metodoPagoService;

    @Test
    void registrarMetodoPago_MetodoNuevo_GuardaExitosamente() {
        // Arrange
        MetodoPagoDTO requestDTO = new MetodoPagoDTO(null, "YAPE", true);
        when(metodoPagoRepo.existsByNombreMetodo("YAPE")).thenReturn(false);

        MetodoPago metodoGuardado = new MetodoPago();
        metodoGuardado.setIdMetodo(5L);
        metodoGuardado.setNombreMetodo("YAPE");
        metodoGuardado.setEsDigital(true);

        when(metodoPagoRepo.save(any(MetodoPago.class))).thenReturn(metodoGuardado);

        // Act
        MetodoPagoDTO response = metodoPagoService.registrarMetodoPago(requestDTO);

        // Assert
        assertEquals(5L, response.idMetodo());
        assertTrue(response.esDigital());
        verify(metodoPagoRepo, times(1)).save(any(MetodoPago.class));
    }

    @Test
    void registrarMetodoPago_NombreExistente_LanzaDuplicadoException() {
        // Arrange
        MetodoPagoDTO requestDTO = new MetodoPagoDTO(null, "EFECTIVO", false);
        when(metodoPagoRepo.existsByNombreMetodo("EFECTIVO")).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicadoException.class, () -> metodoPagoService.registrarMetodoPago(requestDTO),
                "El sistema debe evitar la colisión de nombres en los métodos de pago.");

        verify(metodoPagoRepo, never()).save(any());
    }
}