package pe.edu.utp.sistemadereservacionhotel.service.finanzas;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.sistemadereservacionhotel.dto.finanzas.ImpuestoDTO;
import pe.edu.utp.sistemadereservacionhotel.model.finanzas.Impuesto;
import pe.edu.utp.sistemadereservacionhotel.repository.finanzas.ImpuestoRepository;
import pe.edu.utp.sistemadereservacionhotel.service.finanzas.impl.ImpuestoServiceImpl;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.DuplicadoException;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.RecursoNoEncontradoException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImpuestoServiceImplTest {

    @Mock
    private ImpuestoRepository impuestoRepo;

    @InjectMocks
    private ImpuestoServiceImpl impuestoService;

    @Test
    void registrarImpuesto_DatosValidos_GuardaYRetornaDto() {
        // Arrange
        ImpuestoDTO requestDTO = new ImpuestoDTO(null, "IGV", new BigDecimal("18.00"));
        when(impuestoRepo.existsByNombreImpuesto("IGV")).thenReturn(false);

        Impuesto impuestoGuardado = new Impuesto();
        impuestoGuardado.setIdImpuesto(1L);
        impuestoGuardado.setNombreImpuesto("IGV");
        impuestoGuardado.setPorcentaje(new BigDecimal("18.00"));

        when(impuestoRepo.save(any(Impuesto.class))).thenReturn(impuestoGuardado);

        // Act
        ImpuestoDTO response = impuestoService.registrarImpuesto(requestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.idImpuesto());
        assertEquals("IGV", response.nombreImpuesto());
        verify(impuestoRepo, times(1)).save(any(Impuesto.class));
    }

    @Test
    void registrarImpuesto_NombreDuplicado_LanzaDuplicadoException() {
        // Arrange
        ImpuestoDTO requestDTO = new ImpuestoDTO(null, "ISC", new BigDecimal("10.00"));
        when(impuestoRepo.existsByNombreImpuesto("ISC")).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicadoException.class, () -> impuestoService.registrarImpuesto(requestDTO),
                "Debe bloquear el registro de impuestos con nombres duplicados.");

        verify(impuestoRepo, never()).save(any());
    }

    @Test
    void actualizarImpuesto_IdInexistente_LanzaRecursoNoEncontradoException() {
        // Arrange
        ImpuestoDTO requestDTO = new ImpuestoDTO(99L, "RENTA", new BigDecimal("5.00"));
        when(impuestoRepo.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecursoNoEncontradoException.class, () -> impuestoService.actualizarImpuesto(99L, requestDTO),
                "No se puede actualizar un impuesto fantasma.");

        verify(impuestoRepo, never()).save(any());
    }
}