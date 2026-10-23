package pe.edu.utp.sistemadereservacionhotel.service.patron.creacional;

public class ComprobanteFactorySelector {

    // Constructor privado para ocultar el público implícito (Regla de SonarLint)
    private ComprobanteFactorySelector() {
        throw new IllegalStateException("Clase utilitaria de Factory");
    }

    public static ComprobanteFactory obtenerFactory(String tipo) {
        return switch (tipo.toUpperCase()) {
            case "BOLETA" -> new BoletaFactory();
            case "FACTURA" -> new FacturaFactory();
            default -> throw new IllegalArgumentException("Tipo de comprobante no válido: " + tipo);
        };
    }
}