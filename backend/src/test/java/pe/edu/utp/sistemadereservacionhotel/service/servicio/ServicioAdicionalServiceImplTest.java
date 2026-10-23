package pe.edu.utp.sistemadereservacionhotel.service.servicio.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.sistemadereservacionhotel.dto.servicio.ServicioAdicionalDTO;
import pe.edu.utp.sistemadereservacionhotel.model.servicio.ServicioAdicional;
import pe.edu.utp.sistemadereservacionhotel.repository.servicio.ServicioAdicionalRepository;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.DuplicadoException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicioAdicionalServiceImplTest {

    @Mock
    private ServicioAdicionalRepository servicioRepo;

    @InjectMocks
    private ServicioAdicionalServiceImpl servicioService;

    @Test
    void registrarServicio_DatosValidos_GuardaExitosamente() {
        // Arrange
        ServicioAdicionalDTO dto = new ServicioAdicionalDTO(null, "Lavandería", "Servicio exprés", true);
        when(servicioRepo.existsByNombre("Lavandería")).thenReturn(false);

        ServicioAdicional guardado = new ServicioAdicional();
        guardado.setIdServicio(1L);
        guardado.setNombre("Lavandería");

        when(servicioRepo.save(any(ServicioAdicional.class))).thenReturn(guardado);

        // Act
        ServicioAdicionalDTO response = servicioService.registrarServicio(dto);

        // Assert
        assertNotNull(response);
        assertEquals("Lavandería", response.nombre());
        verify(servicioRepo, times(1)).save(any(ServicioAdicional.class));
    }

    @Test
    void registrarServicio_NombreDuplicado_LanzaDuplicadoException() {
        // Arrange
        ServicioAdicionalDTO dto = new ServicioAdicionalDTO(null, "Spa", "Masajes", true);
        when(servicioRepo.existsByNombre("Spa")).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicadoException.class, () -> servicioService.registrarServicio(dto),
                "No debe permitir servicios duplicados por nombre.");

        verify(servicioRepo, never()).save(any());
    }
}