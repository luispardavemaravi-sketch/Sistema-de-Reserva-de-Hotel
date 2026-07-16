package pe.edu.utp.sistemadereservacionhotel.service.patron.creacional;

import org.junit.jupiter.api.Test;
import pe.edu.utp.sistemadereservacionhotel.model.finanzas.ComprobantePago;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.Reserva;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Suite de pruebas para auditar la precisión contable y generación de series en los comprobantes.
 */
class ComprobanteFactoryTest {

    @Test
    void crearFactura_AplicaIGVCorrectamente_Y_GeneraPrefijoF001() {
        // Arrange
        FacturaFactory factory = new FacturaFactory();
        Reserva reservaMock = new Reserva();
        BigDecimal montoBase = new BigDecimal("100.00");

        // Act
        ComprobantePago factura = factory.crear(reservaMock, montoBase);

        // Assert: 100 * 1.18 = 118.00
        assertEquals(new BigDecimal("118.00"), factura.getMontoTotal(), "La Factura no calculó correctamente el IGV (18%)");
        assertTrue(factura.getNumeroSerie().startsWith("F001-"), "La serie de la factura no tiene el prefijo F001-");
    }

    @Test
    void crearBoleta_MantieneMontoBase_Y_GeneraPrefijoB001() {
        // Arrange
        BoletaFactory factory = new BoletaFactory();
        Reserva reservaMock = new Reserva();
        BigDecimal montoBase = new BigDecimal("100.00");

        // Act
        ComprobantePago boleta = factory.crear(reservaMock, montoBase);

        // Assert: El monto debe mantenerse igual
        assertEquals(new BigDecimal("100.00"), boleta.getMontoTotal(), "La Boleta alteró el monto base, lo cual es incorrecto");
        assertTrue(boleta.getNumeroSerie().startsWith("B001-"), "La serie de la boleta no tiene el prefijo B001-");
    }
}