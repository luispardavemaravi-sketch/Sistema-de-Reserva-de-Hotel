package pe.edu.utp.sistemadereservacionhotel.service.finanzas.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sistemadereservacionhotel.model.finanzas.Promocion;
import pe.edu.utp.sistemadereservacionhotel.repository.finanzas.PromocionRepository;
import pe.edu.utp.sistemadereservacionhotel.service.finanzas.PromocionService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class PromocionServiceImpl implements PromocionService {

    private final PromocionRepository repo;

    @Override
    public Promocion save(Promocion promocion) {
        if (promocion.getIdPromocion() != null)
            throw new IllegalArgumentException("Para actualizar use el método update");
        if (repo.existsByCodigoCupon(promocion.getCodigoCupon())) {
            throw new IllegalArgumentException("Ya existe una promoción con el código: " + promocion.getCodigoCupon());
        }
        if (promocion.getFechaCaducidad().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de caducidad no puede ser anterior a hoy");
        }
        return repo.save(promocion);
    }

    @Override
    public Promocion update(Promocion promocion) {
        if (promocion.getIdPromocion() == null)
            throw new IllegalArgumentException("El ID no puede ser nulo para actualizar");
        Promocion existente = repo.findById(promocion.getIdPromocion())
                .orElseThrow(() -> new RuntimeException("Promoción no encontrada con ID: " + promocion.getIdPromocion()));
        existente.setPorcentajeDescuento(promocion.getPorcentajeDescuento());
        existente.setFechaCaducidad(promocion.getFechaCaducidad());
        return repo.save(existente);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new RuntimeException("Promoción no encontrada con ID: " + id);
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Promocion> findAll() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Promocion> findById(Long id) {
        if (id == null || id <= 0) throw new IllegalArgumentException("El ID debe ser positivo");
        return repo.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Promocion> findByCodigoCupon(String codigo) {
        if (codigo == null || codigo.isBlank()) throw new IllegalArgumentException("El código no puede estar vacío");
        return repo.findByCodigoCupon(codigo.trim().toUpperCase());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Promocion> findVigentes() {
        return repo.findByFechaCaducidadAfter(LocalDate.now());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Promocion> findByPorcentajeMinimo(Double porcentaje) {
        return repo.findByPorcentajeDescuentoGreaterThanEqual(porcentaje);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCodigoCupon(String codigo) {
        return repo.existsByCodigoCupon(codigo.trim().toUpperCase());
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return repo.count();
    }
}