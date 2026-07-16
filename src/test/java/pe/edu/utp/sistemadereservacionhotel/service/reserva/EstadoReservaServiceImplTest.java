package pe.edu.utp.sistemadereservacionhotel.service.reserva;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.sistemadereservacionhotel.dto.reserva.EstadoReservaDTO;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.EstadoReserva;
import pe.edu.utp.sistemadereservacionhotel.repository.reserva.EstadoReservaRepository;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.DuplicadoException;
import pe.edu.utp.sistemadereservacionhotel.service.reserva.impl.EstadoReservaServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstadoReservaServiceImplTest {

    @Mock
    private EstadoReservaRepository estadoRepo;

    @InjectMocks
    private EstadoReservaServiceImpl estadoService;

    @Test
    void registrarEstado_NombreValido_GuardaExitosamente() {
        EstadoReservaDTO dto = new EstadoReservaDTO(null, "PENDIENTE", true);
        when(estadoRepo.existsByNombreEstado("PENDIENTE")).thenReturn(false);

        EstadoReserva entidadGuardada = new EstadoReserva();
        entidadGuardada.setIdEstado(1L);
        entidadGuardada.setNombreEstado("PENDIENTE");

        when(estadoRepo.save(any(EstadoReserva.class))).thenReturn(entidadGuardada);

        EstadoReservaDTO response = estadoService.registrarEstado(dto);

        assertNotNull(response);
        assertEquals("PENDIENTE", response.nombreEstado());
        verify(estadoRepo, times(1)).save(any(EstadoReserva.class));
    }

    @Test
    void registrarEstado_NombreDuplicado_LanzaDuplicadoException() {
        EstadoReservaDTO dto = new EstadoReservaDTO(null, "RESERVADO", true);
        when(estadoRepo.existsByNombreEstado("RESERVADO")).thenReturn(true);

        assertThrows(DuplicadoException.class, () -> estadoService.registrarEstado(dto));
        verify(estadoRepo, never()).save(any());
    }
}