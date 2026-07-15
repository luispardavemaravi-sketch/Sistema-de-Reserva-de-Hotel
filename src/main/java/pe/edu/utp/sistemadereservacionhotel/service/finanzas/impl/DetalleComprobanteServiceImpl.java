package pe.edu.utp.sistemadereservacionhotel.service.finanzas.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sistemadereservacionhotel.model.finanzas.DetalleComprobante;
import pe.edu.utp.sistemadereservacionhotel.repository.finanzas.DetalleComprobanteRepository;
import pe.edu.utp.sistemadereservacionhotel.service.finanzas.DetalleComprobanteService;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.RecursoNoEncontradoException;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.ValidacionException;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class DetalleComprobanteServiceImpl implements DetalleComprobanteService {

    private final DetalleComprobanteRepository repo;

    @Override
    public DetalleComprobante save(DetalleComprobante detalle) {
        if (detalle.getIdDetalle() != null)
            throw new ValidacionException("Para actualizar use el método update");
        detalle.setSubtotalLinea(detalle.getCantidad() * detalle.getPrecioUnitario());
        return repo.save(detalle);
    }

    @Override
    public DetalleComprobante update(DetalleComprobante detalle) {
        if (detalle.getIdDetalle() == null)
            throw new ValidacionException("El ID no puede ser nulo para actualizar");
        DetalleComprobante existente = repo.findById(detalle.getIdDetalle())
                .orElseThrow(() -> new RecursoNoEncontradoException("DetalleComprobante", detalle.getIdDetalle()));
        existente.setCantidad(detalle.getCantidad());
        existente.setDescripcion(detalle.getDescripcion());
        existente.setPrecioUnitario(detalle.getPrecioUnitario());
        existente.setSubtotalLinea(detalle.getCantidad() * detalle.getPrecioUnitario());
        return repo.save(existente);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new RecursoNoEncontradoException("DetalleComprobante", id);
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleComprobante> findAll() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DetalleComprobante> findById(Long id) {
        if (id == null || id <= 0)
            throw new ValidacionException("El ID debe ser positivo");
        return repo.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleComprobante> findByComprobante(Long idComprobante) {
        return repo.findByComprobantePago_IdComprobante(idComprobante);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleComprobante> findByDescripcion(String descripcion) {
        if (descripcion == null || descripcion.isBlank())
            throw new ValidacionException("La descripción no puede estar vacía");
        return repo.findByDescripcionContainingIgnoreCase(descripcion.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return repo.count();
    }
}