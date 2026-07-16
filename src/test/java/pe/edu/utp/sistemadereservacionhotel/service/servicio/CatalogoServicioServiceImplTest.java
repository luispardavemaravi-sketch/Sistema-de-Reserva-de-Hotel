package pe.edu.utp.sistemadereservacionhotel.service.servicio;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.sistemadereservacionhotel.dto.servicio.CatalogoServicioDTO;
import pe.edu.utp.sistemadereservacionhotel.model.servicio.CatalogoServicio;
import pe.edu.utp.sistemadereservacionhotel.model.servicio.CategoriaServicio;
import pe.edu.utp.sistemadereservacionhotel.repository.servicio.CatalogoServicioRepository;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.ValidacionException;
import pe.edu.utp.sistemadereservacionhotel.service.servicio.impl.CatalogoServicioServiceImpl;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CatalogoServicioServiceImplTest {

    @Mock
    private CatalogoServicioRepository catalogoRepo;

    @InjectMocks
    private CatalogoServicioServiceImpl catalogoService;

    @Test
    void registrarEnCatalogo_CategoriaValida_ConvierteEnumCorrectamente() {
        // Arrange: Enviamos "RESTAURANTE" como string, el servicio lo convierte a CategoriaServicio.RESTAURANTE
        CatalogoServicioDTO dto = new CatalogoServicioDTO(null, "RESTAURANTE", new BigDecimal("50.00"));

        CatalogoServicio guardado = new CatalogoServicio();
        guardado.setIdCatalogo(10L);
        guardado.setCategoria(CategoriaServicio.RESTAURANTE);
        guardado.setPrecioVigente(new BigDecimal("50.00"));

        when(catalogoRepo.save(any(CatalogoServicio.class))).thenReturn(guardado);

        // Act
        CatalogoServicioDTO response = catalogoService.registrarEnCatalogo(dto);

        // Assert
        assertEquals("RESTAURANTE", response.categoria());
        verify(catalogoRepo).save(any(CatalogoServicio.class));
    }

    @Test
    void buscarPorPrecioMaximo_PrecioNegativo_LanzaValidacionException() {
        // Act & Assert
        assertThrows(ValidacionException.class, () -> catalogoService.buscarPorPrecioMaximo(new BigDecimal("-1.00")),
                "El sistema debe evitar consultas con montos negativos.");

        verify(catalogoRepo, never()).findByPrecioVigenteLessThanEqual(any());
    }

    @Test
    void registrarEnCatalogo_CategoriaInvalida_LanzaIllegalArgumentException() {
        // Arrange: "EXTRATERRESTRE" no existe en tu Enum CategoriaServicio
        CatalogoServicioDTO dto = new CatalogoServicioDTO(null, "EXTRATERRESTRE", new BigDecimal("10.00"));

        // Act & Assert
        // Java lanzará un IllegalArgumentException al intentar hacer el valueOf()
        assertThrows(IllegalArgumentException.class, () -> catalogoService.registrarEnCatalogo(dto),
                "El servicio debe fallar si la categoría enviada no existe en el catálogo definido.");
    }
}