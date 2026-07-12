package pe.edu.utp.sistemadereservacionhotel.repository.finanzas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.finanzas.DetalleComprobante;

import java.util.List;

@Repository
public interface DetalleComprobanteRepository extends JpaRepository<DetalleComprobante, Long> {
    List<DetalleComprobante> findByComprobantePago_IdComprobante(Long idComprobante);

    List<DetalleComprobante> findByDescripcionContainingIgnoreCase(String descripcion);
}