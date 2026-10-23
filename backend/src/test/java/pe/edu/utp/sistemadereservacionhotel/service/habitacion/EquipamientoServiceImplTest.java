package pe.edu.utp.sistemadereservacionhotel.service.habitacion;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.sistemadereservacionhotel.dto.habitacion.EquipamientoDTO;
import pe.edu.utp.sistemadereservacionhotel.model.habitacion.Equipamiento;
import pe.edu.utp.sistemadereservacionhotel.repository.habitacion.EquipamientoRepository;
import pe.edu.utp.sistemadereservacionhotel.service.habitacion.impl.EquipamientoServiceImpl;


import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EquipamientoServiceImplTest {

    @Mock
    private EquipamientoRepository equipamientoRepo;

    @InjectMocks
    private EquipamientoServiceImpl equipamientoService;

    @Test
    void registrarEquipamiento_DatosValidos_GuardaExitosamente() {
        // Arrange
        EquipamientoDTO requestDTO = new EquipamientoDTO(null, "Proyector 4K", new BigDecimal("50.00"));
        when(equipamientoRepo.existsByNombre("Proyector 4K")).thenReturn(false);

        Equipamiento entidadGuardada = new Equipamiento();
        entidadGuardada.setIdEquipamiento(1L);
        entidadGuardada.setNombre("Proyector 4K");
        entidadGuardada.setCostoAdicional(new BigDecimal("50.00"));

        when(equipamientoRepo.save(any(Equipamiento.class))).thenReturn(entidadGuardada);

        // Act
        EquipamientoDTO response = equipamientoService.registrarEquipamiento(requestDTO);

        // Assert
        assertEquals(1L, response.id());
        assertEquals("Proyector 4K", response.nombre());
        verify(equipamientoRepo, times(1)).save(any(Equipamiento.class));
    }

    @Test
    void buscarPorCostoMaximo_CostoNegativo_LanzaIllegalArgumentExceptionPorFailFast() {
        // Arrange
        BigDecimal costoInvalido = new BigDecimal("-10.00");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> equipamientoService.buscarPorCostoMaximo(costoInvalido),
                "El sistema debe bloquear búsquedas con costos negativos en memoria.");

        verify(equipamientoRepo, never()).findByCostoAdicionalLessThanEqual(any());
    }
}