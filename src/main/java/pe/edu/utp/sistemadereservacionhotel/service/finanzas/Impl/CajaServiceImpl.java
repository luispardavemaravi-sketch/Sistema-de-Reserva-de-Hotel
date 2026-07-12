package pe.edu.utp.sistemadereservacionhotel.service.finanzas.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sistemadereservacionhotel.model.finanzas.Caja;
import pe.edu.utp.sistemadereservacionhotel.repository.finanzas.CajaRepository;
import pe.edu.utp.sistemadereservacionhotel.service.finanzas.CajaService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class CajaServiceImpl implements CajaService {

    private final CajaRepository repo;

    @Override
    public Caja save(Caja caja) {
        if (caja.getIdCaja() != null) {
            throw new IllegalArgumentException("Para actualizar use el método update");
        }
        if (repo.findByEstaAbiertaTrue().isPresent()) {
            throw new IllegalArgumentException("Ya existe una caja abierta");
        }
        caja.setFecha(LocalDate.now());
        caja.setEstaAbierta(true);
        return repo.save(caja);
    }

    @Override
    public Caja update(Caja caja) {
        if (caja.getIdCaja() == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo para actualizar");
        }
        Caja existente = repo.findById(caja.getIdCaja())
                .orElseThrow(() -> new RuntimeException("Caja no encontrada con ID: " + caja.getIdCaja()));
        existente.setMontoCierre(caja.getMontoCierre());
        existente.setEstaAbierta(caja.getEstaAbierta());
        return repo.save(existente);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Caja no encontrada con ID: " + id);
        }
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Caja> findAll() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Caja> findById(Long id) {
        if (id == null || id <= 0) throw new IllegalArgumentException("El ID debe ser positivo");
        return repo.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Caja> findByEmpleado(Long idEmpleado) {
        return repo.findByEmpleado_IdEmpleado(idEmpleado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Caja> findByRangoFecha(LocalDate inicio, LocalDate fin) {
        if (inicio.isAfter(fin)) throw new IllegalArgumentException("Fecha inicio no puede ser posterior a fecha fin");
        return repo.findByFechaBetween(inicio, fin);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Caja> findCajaAbierta() {
        return repo.findByEstaAbiertaTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Caja> findByEstaAbierta(Boolean estaAbierta) {
        return repo.findByEstaAbierta(estaAbierta);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return repo.count();
    }
}