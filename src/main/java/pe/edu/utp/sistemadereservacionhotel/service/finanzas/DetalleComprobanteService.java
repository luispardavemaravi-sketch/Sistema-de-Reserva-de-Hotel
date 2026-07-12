package pe.edu.utp.sistemadereservacionhotel.service.finanzas;

import pe.edu.utp.sistemadereservacionhotel.model.finanzas.DetalleComprobante;

import java.util.List;
import java.util.Optional;

public interface DetalleComprobanteService {
    DetalleComprobante save(DetalleComprobante detalle);

    DetalleComprobante update(DetalleComprobante detalle);

    void delete(Long id);

    List<DetalleComprobante> findAll();

    Optional<DetalleComprobante> findById(Long id);

    List<DetalleComprobante> findByComprobante(Long idComprobante);

    List<DetalleComprobante> findByDescripcion(String descripcion);

    long count();
}
