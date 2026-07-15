package pe.edu.utp.sistemadereservacionhotel.service.habitacion.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sistemadereservacionhotel.model.habitacion.Equipamiento;
import pe.edu.utp.sistemadereservacionhotel.repository.habitacion.EquipamientoRepository;
import pe.edu.utp.sistemadereservacionhotel.service.habitacion.EquipamientoService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class EquipamientoServiceImpl implements EquipamientoService {

    private final EquipamientoRepository repo;

    @Override
    public Equipamiento save(Equipamiento equipamiento) {
        if (equipamiento.getIdEquipamiento() != null) {
            throw new IllegalArgumentException("Para actualizar use el método update");
        }
        if (repo.existsByNombre(equipamiento.getNombre())) {
            throw new IllegalArgumentException("Ya existe un equipamiento con el nombre: " + equipamiento.getNombre());
        }
        return repo.save(equipamiento);
    }

    @Override
    public Equipamiento update(Equipamiento equipamiento) {
        if (equipamiento.getIdEquipamiento() == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo para actualizar");
        }
        Equipamiento existente = repo.findById(equipamiento.getIdEquipamiento())
                .orElseThrow(() -> new RuntimeException("Equipamiento no encontrado con ID: " + equipamiento.getIdEquipamiento()));

        existente.setNombre(equipamiento.getNombre());
        existente.setCostoAdicional(equipamiento.getCostoAdicional());

        return repo.save(existente);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Equipamiento no encontrado con ID: " + id);
        }
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Equipamiento> findAll() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Equipamiento> findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo");
        }
        return repo.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Equipamiento> findByNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        return repo.findByNombre(nombre.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Equipamiento> findByNombreContaining(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        return repo.findByNombreContainingIgnoreCase(nombre.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Equipamiento> findByCostoMaximo(Double costoMaximo) {
        if (costoMaximo < 0) {
            throw new IllegalArgumentException("El costo máximo no puede ser negativo");
        }
        return repo.findByCostoAdicionalLessThanEqual(costoMaximo);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNombre(String nombre) {
        return repo.existsByNombre(nombre.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return repo.count();
    }
}