package pe.edu.utp.sistemadereservacionhotel.service.huesped;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.sistemadereservacionhotel.dto.huesped.HuespedRequestDTO;
import pe.edu.utp.sistemadereservacionhotel.dto.huesped.HuespedResponseDTO;
import pe.edu.utp.sistemadereservacionhotel.model.huesped.Huesped;
import pe.edu.utp.sistemadereservacionhotel.repository.huesped.HuespedRepository;
import pe.edu.utp.sistemadereservacionhotel.service.huesped.impl.HuespedServiceImpl;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.DuplicadoException;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.ValidacionException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HuespedServiceImplTest {

    @Mock
    private HuespedRepository huespedRepo;

    @InjectMocks
    private HuespedServiceImpl huespedService;

    @Test
    void registrarHuesped_DatosNuevosYValidos_GuardaExitosamente() {
        // Arrange
        HuespedRequestDTO requestDTO = new HuespedRequestDTO("Luis", "Pardave", "70000000", "luis@mail.com", "999999999");

        when(huespedRepo.existsByEmail("luis@mail.com")).thenReturn(false);
        when(huespedRepo.existsByDocumentoIdentidad("70000000")).thenReturn(false);

        Huesped entidadGuardada = new Huesped();
        entidadGuardada.setIdHuesped(1L);
        entidadGuardada.setNombre("Luis");
        entidadGuardada.setApellidos("Pardave");
        entidadGuardada.setDocumentoIdentidad("70000000");
        entidadGuardada.setEmail("luis@mail.com");
        entidadGuardada.setEstadoActivo(true);

        when(huespedRepo.save(any(Huesped.class))).thenReturn(entidadGuardada);

        // Act
        HuespedResponseDTO response = huespedService.registrarHuesped(requestDTO);

        // Assert
        assertNotNull(response);
        assertTrue(response.estadoActivo(), "El huésped debe nacer con estado activo obligatorio.");
        assertEquals("luis@mail.com", response.email(), "El mapeo de correo falló.");
        verify(huespedRepo, times(1)).save(any(Huesped.class));
    }

    @Test
    void registrarHuesped_EmailDuplicado_LanzaDuplicadoException() {
        // Arrange
        HuespedRequestDTO requestDTO = new HuespedRequestDTO("Ana", "Gomez", "71111111", "usado@mail.com", "988888888");
        when(huespedRepo.existsByEmail("usado@mail.com")).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicadoException.class, () -> huespedService.registrarHuesped(requestDTO),
                "Debe bloquear el registro si el correo ya pertenece a otro perfil.");

        verify(huespedRepo, never()).existsByDocumentoIdentidad(any());
        verify(huespedRepo, never()).save(any());
    }

    @Test
    void darDeBajaHuesped_PerfilYaInactivo_LanzaValidacionException() {
        // Arrange
        Long idHuesped = 5L;
        Huesped huespedInactivo = new Huesped();
        huespedInactivo.setIdHuesped(idHuesped);
        huespedInactivo.setEstadoActivo(false); // Estado inválido para dar de baja

        when(huespedRepo.findById(idHuesped)).thenReturn(Optional.of(huespedInactivo));

        // Act & Assert
        assertThrows(ValidacionException.class, () -> huespedService.darDeBajaHuesped(idHuesped),
                "El sistema no debe permitir dar de baja a un usuario que ya está inactivo.");

        verify(huespedRepo, never()).save(any());
    }
}