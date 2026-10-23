package pe.edu.utp.sistemadereservacionhotel.service.reserva;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.sistemadereservacionhotel.dto.reserva.CheckInRequestDTO;
import pe.edu.utp.sistemadereservacionhotel.dto.reserva.CheckInResponseDTO;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.CheckIn;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.Reserva;
import pe.edu.utp.sistemadereservacionhotel.repository.reserva.CheckInRepository;
import pe.edu.utp.sistemadereservacionhotel.repository.reserva.ReservaRepository;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.DuplicadoException;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.ValidacionException;
import pe.edu.utp.sistemadereservacionhotel.service.reserva.impl.CheckInServiceImpl;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckInServiceImplTest {

    @Mock
    private CheckInRepository checkInRepo;

    @Mock
    private ReservaRepository reservaRepo;

    @InjectMocks
    private CheckInServiceImpl checkInService;

    @Test
    void realizarCheckIn_ReservaValida_CreaCheckInExitosamente() {
        // Arrange
        CheckInRequestDTO dto = new CheckInRequestDTO(1L, "CheckIn sin observaciones");
        Reserva reservaMock = new Reserva();
        reservaMock.setIdReserva(1L);

        when(reservaRepo.findById(1L)).thenReturn(Optional.of(reservaMock));
        when(checkInRepo.findByReserva_IdReserva(1L)).thenReturn(Optional.empty());

        CheckIn checkInGuardado = new CheckIn();
        checkInGuardado.setIdCheckIn(5L);
        checkInGuardado.setReserva(reservaMock);
        checkInGuardado.setFechaHoraRealEntrada(LocalDateTime.now());

        when(checkInRepo.save(any(CheckIn.class))).thenReturn(checkInGuardado);

        // Act
        CheckInResponseDTO response = checkInService.realizarCheckIn(dto);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.idReserva(), "El ID de reserva vinculado no coincide.");
        verify(checkInRepo, times(1)).save(any(CheckIn.class));
    }

    @Test
    void realizarCheckIn_CheckInPreexistente_LanzaDuplicadoException() {
        // Arrange
        CheckInRequestDTO dto = new CheckInRequestDTO(1L, "Otro checkin");
        Reserva reservaMock = new Reserva();
        reservaMock.setIdReserva(1L);

        when(reservaRepo.findById(1L)).thenReturn(Optional.of(reservaMock));
        when(checkInRepo.findByReserva_IdReserva(1L)).thenReturn(Optional.of(new CheckIn()));

        // Act & Assert
        assertThrows(DuplicadoException.class, () -> checkInService.realizarCheckIn(dto),
                "No debe permitirse duplicar ingresos para una misma reserva.");

        verify(checkInRepo, never()).save(any());
    }

    @Test
    void buscarPorRangoFecha_FechaInvertida_LanzaValidacionException() {
        // Arrange
        LocalDateTime inicio = LocalDateTime.of(2026, 12, 31, 0, 0);
        LocalDateTime fin = LocalDateTime.of(2026, 1, 1, 0, 0);

        // Act & Assert
        assertThrows(ValidacionException.class, () -> checkInService.buscarPorRangoFecha(inicio, fin),
                "Debe validar coherencia temporal en el rango de fechas antes de consultar BD.");
    }
}