package pe.edu.utp.sistemadereservacionhotel.service.servicio;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.sistemadereservacionhotel.dto.servicio.ConsumoServicioDTO;
import pe.edu.utp.sistemadereservacionhotel.model.servicio.*;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.Reserva;
import pe.edu.utp.sistemadereservacionhotel.repository.reserva.ReservaRepository;
import pe.edu.utp.sistemadereservacionhotel.repository.servicio.*;
import pe.edu.utp.sistemadereservacionhotel.service.servicio.impl.ConsumoServicioServiceImpl;

import java.math.BigDecimal;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsumoServicioServiceImplTest {

    @Mock
    private ConsumoServicioRepository consumoRepo;
    @Mock
    private ReservaRepository reservaRepo;
    @Mock
    private ServicioAdicionalRepository servicioRepo;
    @Mock
    private CatalogoServicioRepository catalogoRepo;

    @InjectMocks
    private ConsumoServicioServiceImpl consumoService;

    @Test
    void registrarConsumo_DatosValidos_CalculaSubtotalCorrectamente() {
        // Arrange
        ConsumoServicioDTO dto = new ConsumoServicioDTO(null, 1L, 1L, 2, null, null);

        Reserva reserva = new Reserva();
        reserva.setIdReserva(1L);
        ServicioAdicional servicio = new ServicioAdicional();
        servicio.setIdServicio(1L);

        CatalogoServicio catalogo = new CatalogoServicio();
        catalogo.setPrecioVigente(new BigDecimal("20.00"));

        when(reservaRepo.findById(1L)).thenReturn(Optional.of(reserva));
        when(servicioRepo.findById(1L)).thenReturn(Optional.of(servicio));
        // El repositorio de catálogo devuelve una lista
        when(catalogoRepo.findByServicioAdicional_IdServicio(1L)).thenReturn(List.of(catalogo));

        when(consumoRepo.save(any(ConsumoServicio.class))).thenAnswer(invocation -> {
            ConsumoServicio guardado = invocation.getArgument(0);
            guardado.setIdConsumo(100L);
            return guardado;
        });

        // Act
        var response = consumoService.registrarConsumo(dto);

        // Assert
        assertEquals(new BigDecimal("40.00"), response.subTotal());
        assertEquals(1L, response.idReserva());
        var consumoCaptor = org.mockito.ArgumentCaptor.forClass(ConsumoServicio.class);
        verify(consumoRepo).save(consumoCaptor.capture());
        assertSame(reserva, consumoCaptor.getValue().getReserva());
    }

    @Test
    void registrarConsumo_CatalogoVacio_LanzaRuntimeException() {
        // Arrange
        ConsumoServicioDTO dto = new ConsumoServicioDTO(null, 1L, 1L, 1, null, null);
        when(reservaRepo.findById(1L)).thenReturn(Optional.of(new Reserva()));
        when(servicioRepo.findById(1L)).thenReturn(Optional.of(new ServicioAdicional()));
        // Simulamos que no hay precio en el catálogo (lista vacía)
        when(catalogoRepo.findByServicioAdicional_IdServicio(1L)).thenReturn(List.of());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> consumoService.registrarConsumo(dto),
                "Debe lanzar excepción si el servicio no tiene precio configurado.");
    }
}
