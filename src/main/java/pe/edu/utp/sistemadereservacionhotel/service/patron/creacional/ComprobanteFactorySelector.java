package pe.edu.utp.sistemadereservacionhotel.service.patron.creacional;

public class ComprobanteFactorySelector {

    public static ComprobanteFactory obtenerFactory(String tipo) {
        return switch (tipo.toUpperCase()) {
            case "BOLETA" -> new BoletaFactory();
            case "FACTURA" -> new FacturaFactory();
            default -> throw new IllegalArgumentException("Tipo de comprobante no válido: " + tipo);
        };
    }
}