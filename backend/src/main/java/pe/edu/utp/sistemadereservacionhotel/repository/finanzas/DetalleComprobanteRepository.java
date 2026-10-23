package pe.edu.utp.sistemadereservacionhotel.repository.finanzas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.finanzas.DetalleComprobante;

import java.util.List;

/**
 * Repositorio para la consulta de líneas de detalle facturadas.
 */
@Repository
public interface DetalleComprobanteRepository extends JpaRepository<DetalleComprobante, Long> {

    /**
     * Recupera todas las líneas de detalle pertenecientes a un comprobante específico.
     */
    List<DetalleComprobante> findByComprobantePago_IdComprobante(Long idComprobante);

    /**
     * Busca detalles facturados que contengan un término específico en su descripción,
     * ignorando mayúsculas y minúsculas (Búsqueda de texto).
     */
    List<DetalleComprobante> findByDescripcionContainingIgnoreCase(String descripcion);
}