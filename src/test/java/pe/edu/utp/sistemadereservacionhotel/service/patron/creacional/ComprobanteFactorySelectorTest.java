package pe.edu.utp.sistemadereservacionhotel.service.patron.creacional;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Suite de pruebas para verificar el enrutamiento de la fábrica de comprobantes.
 */
class ComprobanteFactorySelectorTest {

    @Test
    void obtenerFactory_TipoFactura_RetornaFacturaFactory() {
        // Act
        ComprobanteFactory factory = ComprobanteFactorySelector.obtenerFactory("FACTURA");

        // Assert
        assertInstanceOf(FacturaFactory.class, factory, "El selector no devolvió la instancia correcta para FACTURA");
    }

    @Test
    void obtenerFactory_TipoBoletaMinusculas_RetornaBoletaFactory() {
        // Act (probando también la insensibilidad a mayúsculas/minúsculas)
        ComprobanteFactory factory = ComprobanteFactorySelector.obtenerFactory("boleta");

        // Assert
        assertInstanceOf(BoletaFactory.class, factory, "El selector falló al procesar el tipo BOLETA en minúsculas");
    }

    @Test
    void obtenerFactory_TipoDesconocido_LanzaIllegalArgumentException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            ComprobanteFactorySelector.obtenerFactory("TICKET");
        }, "El selector debe rechazar tipos de comprobantes no soportados");
    }


}