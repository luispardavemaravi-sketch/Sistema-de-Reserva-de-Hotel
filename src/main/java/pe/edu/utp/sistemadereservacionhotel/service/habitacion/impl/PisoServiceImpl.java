package pe.edu.utp.sistemadereservacionhotel.service.habitacion.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sistemadereservacionhotel.model.habitacion.Piso;
import pe.edu.utp.sistemadereservacionhotel.repository.habitacion.PisoRepository;
import pe.edu.utp.sistemadereservacionhotel.service.habitacion.PisoService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class PisoServiceImpl implements PisoService {

    private final PisoRepository repo;

    @Override
    public Piso save(Piso piso) {
        if (piso.getIdPiso() != null) {
            throw new IllegalArgumentException("Para actualizar use el método update");
        }
        if (repo.existsByNumeroPiso(piso.getNumeroPiso())) {
            throw new IllegalArgumentException("Ya existe el piso número: " + piso.getNumeroPiso());
        }
        return repo.save(piso);
    }

    @Override
    public Piso update(Piso piso) {
        if (piso.getIdPiso() == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo para actualizar");
        }
        Piso existente = repo.findById(piso.getIdPiso())
                .orElseThrow(() -> new RuntimeException("Piso no encontrado con ID: " + piso.getIdPiso()));

        existente.setNumeroPiso(piso.getNumeroPiso());
        existente.setSector(piso.getSector());

        return repo.save(existente);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Piso no encontrado con ID: " + id);
        }
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Piso> findAll() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Piso> findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo");
        }
        return repo.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Piso> findByNumeroPiso(Integer numeroPiso) {
        if (numeroPiso == null || numeroPiso <= 0) {
            throw new IllegalArgumentException("El número de piso debe ser mayor a cero");
        }
        return repo.findByNumeroPiso(numeroPiso);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Piso> findBySector(String sector) {
        if (sector == null || sector.isBlank()) {
            throw new IllegalArgumentException("El sector no puede estar vacío");
        }
        return repo.findBySectorContainingIgnoreCase(sector.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNumeroPiso(Integer numeroPiso) {
        return repo.existsByNumeroPiso(numeroPiso);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return repo.count();
    }
}