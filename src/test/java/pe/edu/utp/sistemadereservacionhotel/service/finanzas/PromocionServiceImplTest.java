package pe.edu.utp.sistemadereservacionhotel.service.finanzas;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.sistemadereservacionhotel.dto.finanzas.PromocionDTO;
import pe.edu.utp.sistemadereservacionhotel.model.finanzas.Promocion;
import pe.edu.utp.sistemadereservacionhotel.repository.finanzas.PromocionRepository;
import pe.edu.utp.sistemadereservacionhotel.service.finanzas.impl.PromocionServiceImpl;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.DuplicadoException;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.ValidacionException;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PromocionServiceImplTest {

    @Mock
    private PromocionRepository promocionRepo;

    @InjectMocks
    private PromocionServiceImpl promocionService;

    @Test
    void registrarPromocion_DatosValidos_AplicaMayusculasYGuarda() {
        // Arrange
        // Usamos una fecha futura extrema para asegurar que no caduque en los tests
        LocalDate fechaFutura = LocalDate.of(2050, 12, 31);
        PromocionDTO requestDTO = new PromocionDTO(null, "verano50", new BigDecimal("50.00"), fechaFutura);

        when(promocionRepo.existsByCodigoCupon("verano50")).thenReturn(false);

        Promocion promocionGuardada = new Promocion();
        promocionGuardada.setIdPromocion(1L);
        promocionGuardada.setCodigoCupon("VERANO50"); // El servicio debe convertir a mayúsculas
        promocionGuardada.setPorcentajeDescuento(new BigDecimal("50.00"));
        promocionGuardada.setFechaCaducidad(fechaFutura);

        when(promocionRepo.save(any(Promocion.class))).thenReturn(promocionGuardada);

        // Act
        PromocionDTO response = promocionService.registrarPromocion(requestDTO);

        // Assert
        assertEquals("VERANO50", response.codigoCupon(), "El servicio no estandarizó el cupón a mayúsculas.");
        verify(promocionRepo, times(1)).save(any(Promocion.class));
    }

    @Test
    void registrarPromocion_FechaPasada_LanzaValidacionExceptionPorFailFast() {
        // Arrange
        // Forzamos una fecha en el pasado
        LocalDate fechaPasada = LocalDate.of(2020, 1, 1);
        PromocionDTO requestDTO = new PromocionDTO(null, "BLACKFRIDAY", new BigDecimal("20.00"), fechaPasada);

        when(promocionRepo.existsByCodigoCupon("BLACKFRIDAY")).thenReturn(false);

        // Act & Assert
        assertThrows(ValidacionException.class, () -> promocionService.registrarPromocion(requestDTO),
                "Debe rechazar la creación de cupones que ya nacen caducados.");

        verify(promocionRepo, never()).save(any());
    }

    @Test
    void registrarPromocion_CodigoDuplicado_LanzaDuplicadoException() {
        // Arrange
        LocalDate fechaFutura = LocalDate.of(2050, 12, 31);
        PromocionDTO requestDTO = new PromocionDTO(null, "VIP10", new BigDecimal("10.00"), fechaFutura);

        when(promocionRepo.existsByCodigoCupon("VIP10")).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicadoException.class, () -> promocionService.registrarPromocion(requestDTO),
                "El sistema debe evitar colisiones en códigos promocionales.");

        verify(promocionRepo, never()).save(any());
    }
}