package pe.edu.utp.sistemadereservacionhotel.service.reserva;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.sistemadereservacionhotel.dto.reserva.CheckOutRequestDTO;
import pe.edu.utp.sistemadereservacionhotel.dto.reserva.CheckOutResponseDTO;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.CheckOut;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.Reserva;
import pe.edu.utp.sistemadereservacionhotel.repository.reserva.CheckOutRepository;
import pe.edu.utp.sistemadereservacionhotel.repository.reserva.ReservaRepository;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.ValidacionException;
import pe.edu.utp.sistemadereservacionhotel.service.reserva.impl.CheckOutServiceImpl;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckOutServiceImplTest {

    @Mock
    private CheckOutRepository checkOutRepo;

    @Mock
    private ReservaRepository reservaRepo;

    @InjectMocks
    private CheckOutServiceImpl checkOutService;

    @Test
    void realizarCheckOut_DatosValidos_GuardaConMulta() {
        // Arrange
        CheckOutRequestDTO dto = new CheckOutRequestDTO(1L, new BigDecimal("50.00"), "Retraso de 2 horas");
        Reserva reservaMock = new Reserva();
        reservaMock.setIdReserva(1L);

        when(reservaRepo.findById(1L)).thenReturn(Optional.of(reservaMock));
        when(checkOutRepo.findByReserva_IdReserva(1L)).thenReturn(Optional.empty());

        CheckOut guardado = new CheckOut();
        guardado.setIdCheckOut(10L);
        guardado.setReserva(reservaMock);
        guardado.setMultaPorRetraso(new BigDecimal("50.00"));

        when(checkOutRepo.save(any(CheckOut.class))).thenReturn(guardado);

        // Act
        CheckOutResponseDTO response = checkOutService.realizarCheckOut(dto);

        // Assert
        assertNotNull(response);
        assertEquals(new BigDecimal("50.00"), response.multaAplicada(), "El monto de la multa no se persistió correctamente.");
        verify(checkOutRepo, times(1)).save(any(CheckOut.class));
    }

    @Test
    void buscarConMulta_MontoNegativo_LanzaValidacionException() {
        // Arrange
        BigDecimal montoNegativo = new BigDecimal("-100.00");

        // Act & Assert
        assertThrows(ValidacionException.class, () -> checkOutService.buscarConMulta(montoNegativo),
                "El sistema debe bloquear búsquedas de multas con montos inválidos.");

        verify(checkOutRepo, never()).findByMultaPorRetrasoGreaterThan(any());
    }
}