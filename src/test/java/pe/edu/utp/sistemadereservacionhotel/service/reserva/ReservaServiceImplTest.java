package pe.edu.utp.sistemadereservacionhotel.service.reserva;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.sistemadereservacionhotel.dto.reserva.ReservaRequestDTO;

import pe.edu.utp.sistemadereservacionhotel.model.reserva.CheckIn;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.EstadoReserva;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.Reserva;
import pe.edu.utp.sistemadereservacionhotel.repository.habitacion.HabitacionRepository;
import pe.edu.utp.sistemadereservacionhotel.repository.huesped.HuespedRepository;
import pe.edu.utp.sistemadereservacionhotel.repository.reserva.EstadoReservaRepository;
import pe.edu.utp.sistemadereservacionhotel.repository.reserva.ReservaRepository;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.ValidacionException;
import pe.edu.utp.sistemadereservacionhotel.service.reserva.impl.ReservaServiceImpl;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceImplTest {

    @Mock private ReservaRepository repo;
    @Mock private HuespedRepository huespedRepo;
    @Mock private HabitacionRepository habitacionRepo;
    @Mock private EstadoReservaRepository estadoRepo;

    @InjectMocks
    private ReservaServiceImpl reservaService;

    @Test
    void crearReserva_FechasInvertidas_LanzaIllegalArgumentException() {
        ReservaRequestDTO dto = new ReservaRequestDTO(
                1L, 1L,
                LocalDate.now().plusDays(5), // Entrada
                LocalDate.now().plusDays(2)  // Salida (Antes de entrada)
        );

        assertThrows(IllegalArgumentException.class, () -> reservaService.crearReserva(dto),
                "El sistema debe bloquear rangos de fechas donde la salida precede a la entrada.");
    }

    @Test
    void cancelarReserva_ConCheckInExistente_LanzaValidacionException() {
        // Arrange
        Long idReserva = 10L;
        Reserva reserva = new Reserva();
        reserva.setCheckIn(new CheckIn()); // Esto simula que la reserva ya inició

        when(repo.findById(idReserva)).thenReturn(Optional.of(reserva));

        // Act & Assert
        assertThrows(ValidacionException.class, () -> reservaService.cancelarReserva(idReserva),
                "No debe permitirse cancelar una reserva que ya ha sido iniciada con un Check-In.");
    }

    @Test
    void cancelarReserva_ReservaValida_CambiaEstadoACancelado() {
        // Arrange
        Long idReserva = 10L;
        Reserva reserva = new Reserva();
        reserva.setCheckIn(null); // Sin check-in, es cancelable

        when(repo.findById(idReserva)).thenReturn(Optional.of(reserva));

        EstadoReserva estadoCancelado = new EstadoReserva();
        estadoCancelado.setIdEstado(3L);
        when(estadoRepo.findById(3L)).thenReturn(Optional.of(estadoCancelado));

        // Act
        reservaService.cancelarReserva(idReserva);

        // Assert
        assertEquals(3L, reserva.getEstadoReserva().getIdEstado());
        verify(repo).save(reserva);
    }
}