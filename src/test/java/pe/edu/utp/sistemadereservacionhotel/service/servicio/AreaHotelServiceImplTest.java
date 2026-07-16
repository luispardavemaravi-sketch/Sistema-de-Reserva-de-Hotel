package pe.edu.utp.sistemadereservacionhotel.service.servicio;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.sistemadereservacionhotel.dto.servicio.AreaHotelDTO;
import pe.edu.utp.sistemadereservacionhotel.model.servicio.AreaHotel;
import pe.edu.utp.sistemadereservacionhotel.repository.servicio.AreaHotelRepository;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.DuplicadoException;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.ValidacionException;
import pe.edu.utp.sistemadereservacionhotel.service.servicio.impl.AreaHotelServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AreaHotelServiceImplTest {

    @Mock
    private AreaHotelRepository areaRepo;

    @InjectMocks
    private AreaHotelServiceImpl areaService;

    @Test
    void registrarArea_NombreValido_GuardaExitosamente() {
        // Arrange
        AreaHotelDTO dto = new AreaHotelDTO(null, "Recepción", "Lobby Principal");
        when(areaRepo.existsByNombreArea("Recepción")).thenReturn(false);

        AreaHotel entidadGuardada = new AreaHotel();
        entidadGuardada.setIdAreaHotel(1L);
        when(areaRepo.save(any(AreaHotel.class))).thenReturn(entidadGuardada);

        // Act
        AreaHotelDTO response = areaService.registrarArea(dto);

        // Assert
        assertNotNull(response);
        verify(areaRepo, times(1)).save(any(AreaHotel.class));
    }

    @Test
    void registrarArea_NombreDuplicado_LanzaDuplicadoException() {
        // Arrange
        AreaHotelDTO dto = new AreaHotelDTO(null, "Cocina", "Sótano");
        when(areaRepo.existsByNombreArea("Cocina")).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicadoException.class, () -> areaService.registrarArea(dto),
                "No debe permitir registrar áreas con nombres duplicados.");

        verify(areaRepo, never()).save(any());
    }

    @Test
    void buscarPorUbicacion_CampoVacio_LanzaValidacionException() {
        // Act & Assert
        assertThrows(ValidacionException.class, () -> areaService.buscarPorUbicacion(" "),
                "La búsqueda debe fallar si la ubicación es un campo en blanco.");

        verify(areaRepo, never()).findByUbicacionContainingIgnoreCase(any());
    }
}