package pe.edu.utp.sistemadereservacionhotel.service.patron.estructural;

import org.junit.jupiter.api.Test;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.Reserva;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Suite de pruebas unitarias para auditar el patrón Estructural Decorator.
 * Evalúa la composición dinámica y la recursividad de los cálculos financieros.
 */
class ReservaDecoratorTest {

    @Test
    void calcularCostoTotal_SoloReservaBase_RetornaMontoBase() {
        // Arrange
        Reserva reservaMock = new Reserva();
        reservaMock.setCodigoReserva("RES-BASE-01");
        reservaMock.setMontoTotalEstimado(new BigDecimal("200.00"));

        ReservaDecorator reservaBase = new ReservaBase(reservaMock);

        // Act
        double costoTotal = reservaBase.calcularCostoTotal();
        String descripcion = reservaBase.obtenerDescripcion();

        // Assert
        assertEquals(200.0, costoTotal, "La reserva base alteró el cálculo original.");
        assertEquals("Reserva base: RES-BASE-01", descripcion, "La descripción base no coincide con el formato esperado.");
    }

    @Test
    void calcularCostoTotal_ConUnSoloDecorador_SumaCostoCorrectamente() {
        // Arrange
        Reserva reservaMock = new Reserva();
        reservaMock.setCodigoReserva("RES-DESC-02");
        reservaMock.setMontoTotalEstimado(new BigDecimal("100.00"));

        ReservaDecorator reservaBase = new ReservaBase(reservaMock);
        ReservaDecorator conDesayuno = new DesayunoDecorator(reservaBase);

        // Act
        double costoTotal = conDesayuno.calcularCostoTotal();
        String descripcion = conDesayuno.obtenerDescripcion();

        // Assert: 100 (Base) + 25 (Desayuno) = 125.0
        assertEquals(125.0, costoTotal, "El cálculo falló al apilar un solo decorador (Desayuno).");
        assertTrue(descripcion.contains("RES-DESC-02"), "Se perdió el código de la reserva base.");
        assertTrue(descripcion.contains("Desayuno"), "No se concatenó la descripción del Desayuno.");
    }

    @Test
    void calcularCostoTotal_ConMultiplesDecoradoresApilados_MantieneIntegridadRecursiva() {
        // Arrange: Probamos la máxima complejidad del patrón
        Reserva reservaMock = new Reserva();
        reservaMock.setCodigoReserva("RES-COMP-03");
        reservaMock.setMontoTotalEstimado(new BigDecimal("150.00"));

        // Apilamiento de capas
        ReservaDecorator reservaBase = new ReservaBase(reservaMock);
        ReservaDecorator capa1 = new DesayunoDecorator(reservaBase);
        ReservaDecorator capa2 = new EstacionamientoDecorator(capa1);
        ReservaDecorator capa3 = new SpaDecorator(capa2);

        // Act
        double costoTotal = capa3.calcularCostoTotal();
        String descripcion = capa3.obtenerDescripcion();

        // Assert: 150 (Base) + 25 (Desayuno) + 15 (Estacionamiento) + 80 (Spa) = 270.0
        assertEquals(270.0, costoTotal, "Fallo crítico en la recursividad matemática de múltiples decoradores.");

        // Verificación de integridad de la cadena
        assertTrue(descripcion.contains("RES-COMP-03"), "La capa profunda perdió la referencia de la base.");
        assertTrue(descripcion.contains("Desayuno"), "Se perdió el registro del desayuno en la composición.");
        assertTrue(descripcion.contains("Estacionamiento"), "Se perdió el registro del estacionamiento en la composición.");
        assertTrue(descripcion.contains("Spa"), "Se perdió el registro del spa en la composición.");
    }

    @Test
    void calcularCostoTotal_ReservaBaseSinMontoDefinido_ManejaNullPointerDevolviendoCero() {
        // Arrange: Simulación de caso borde (Edge Case) donde el monto es null en la base de datos
        Reserva reservaNula = new Reserva();
        reservaNula.setCodigoReserva("RES-NULL");
        reservaNula.setMontoTotalEstimado(null);

        ReservaDecorator reservaBase = new ReservaBase(reservaNula);
        ReservaDecorator conSpa = new SpaDecorator(reservaBase);

        // Act
        double costoTotal = conSpa.calcularCostoTotal();

        // Assert: 0.0 (Base) + 80 (Spa) = 80.0
        assertEquals(80.0, costoTotal, "El sistema no manejó correctamente un monto base nulo, riesgo de NullPointerException.");
    }
}