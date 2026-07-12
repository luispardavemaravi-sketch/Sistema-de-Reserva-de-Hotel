package pe.edu.utp.sistemadereservacionhotel.service.finanzas.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sistemadereservacionhotel.model.finanzas.MetodoPago;
import pe.edu.utp.sistemadereservacionhotel.repository.finanzas.MetodoPagoRepository;
import pe.edu.utp.sistemadereservacionhotel.service.finanzas.MetodoPagoService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class MetodoPagoServiceImpl implements MetodoPagoService {

    private final MetodoPagoRepository repo;

    @Override
    public MetodoPago save(MetodoPago metodoPago) {
        if (metodoPago.getIdMetodo() != null)
            throw new IllegalArgumentException("Para actualizar use el método update");
        if (repo.existsByNombreMetodo(metodoPago.getNombreMetodo())) {
            throw new IllegalArgumentException("Ya existe el método de pago: " + metodoPago.getNombreMetodo());
        }
        return repo.save(metodoPago);
    }

    @Override
    public MetodoPago update(MetodoPago metodoPago) {
        if (metodoPago.getIdMetodo() == null)
            throw new IllegalArgumentException("El ID no puede ser nulo para actualizar");
        MetodoPago existente = repo.findById(metodoPago.getIdMetodo())
                .orElseThrow(() -> new RuntimeException("Método de pago no encontrado con ID: " + metodoPago.getIdMetodo()));
        existente.setNombreMetodo(metodoPago.getNombreMetodo());
        existente.setEsDigital(metodoPago.getEsDigital());
        return repo.save(existente);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new RuntimeException("Método de pago no encontrado con ID: " + id);
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MetodoPago> findAll() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MetodoPago> findById(Long id) {
        if (id == null || id <= 0) throw new IllegalArgumentException("El ID debe ser positivo");
        return repo.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MetodoPago> findByNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("El nombre no puede estar vacío");
        return repo.findByNombreMetodo(nombre.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MetodoPago> findByEsDigital(Boolean esDigital) {
        return repo.findByEsDigital(esDigital);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNombre(String nombre) {
        return repo.existsByNombreMetodo(nombre.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return repo.count();
    }
}