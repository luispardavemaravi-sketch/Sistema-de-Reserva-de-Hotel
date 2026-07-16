package pe.edu.utp.sistemadereservacionhotel.service.reserva;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.sistemadereservacionhotel.dto.reserva.AcompananteRequestDTO;
import pe.edu.utp.sistemadereservacionhotel.dto.reserva.AcompananteResponseDTO;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.Acompanante;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.Parentesco;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.Reserva;
import pe.edu.utp.sistemadereservacionhotel.repository.reserva.AcompananteRepository;
import pe.edu.utp.sistemadereservacionhotel.repository.reserva.ReservaRepository;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.ValidacionException;
import pe.edu.utp.sistemadereservacionhotel.service.reserva.impl.AcompananteServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AcompananteServiceImplTest {

    @Mock
    private AcompananteRepository acompananteRepo;

    @Mock
    private ReservaRepository reservaRepo;

    @InjectMocks
    private AcompananteServiceImpl acompananteService;

    @Test
    void registrarAcompanante_DatosValidos_GuardaExitosamente() {
        // Arrange: Enviamos los 6 parámetros.
        // Null para el ID del acompañante y 1L al final para el ID de la reserva.
        AcompananteRequestDTO dto = new AcompananteRequestDTO(null, "Juan", "Perez", "12345678", "HIJO", 1L);

        Reserva reservaMock = new Reserva();
        reservaMock.setIdReserva(1L);

        when(reservaRepo.findById(1L)).thenReturn(Optional.of(reservaMock));

        Acompanante guardado = new Acompanante();
        guardado.setIdAcompanante(10L);
        guardado.setNombreCompleto("Juan");
        guardado.setParentesco(Parentesco.HIJO);
        guardado.setReserva(reservaMock);

        when(acompananteRepo.save(any(Acompanante.class))).thenReturn(guardado);

        // Act
        AcompananteResponseDTO response = acompananteService.registrarAcompanante(dto);

        // Assert
        assertNotNull(response);
        assertEquals("HIJO", response.parentesco());
        verify(acompananteRepo, times(1)).save(any(Acompanante.class));
    }

    @Test
    void registrarAcompanante_ParentescoInvalido_LanzaValidacionException() {
        // Arrange: Aquí también enviamos los 6 parámetros.
        AcompananteRequestDTO dto = new AcompananteRequestDTO(null, "Luis", "Maravi", "71359913", "ALIENIGENA", 1L);

        Reserva reservaMock = new Reserva();
        reservaMock.setIdReserva(1L);

        // Es vital que el ID (1L) coincida con el que pusimos en el DTO arriba
        when(reservaRepo.findById(1L)).thenReturn(Optional.of(reservaMock));

        // Act & Assert
        assertThrows(ValidacionException.class, () -> acompananteService.registrarAcompanante(dto),
                "El sistema debe bloquear parentescos no definidos en el Enum.");

        verify(acompananteRepo, never()).save(any());
    }
}