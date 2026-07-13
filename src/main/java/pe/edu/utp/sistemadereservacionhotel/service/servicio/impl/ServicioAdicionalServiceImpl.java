package pe.edu.utp.sistemadereservacionhotel.service.servicio.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sistemadereservacionhotel.model.servicio.ServicioAdicional;
import pe.edu.utp.sistemadereservacionhotel.repository.servicio.ServicioAdicionalRepository;
import pe.edu.utp.sistemadereservacionhotel.service.servicio.ServicioAdicionalService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class ServicioAdicionalServiceImpl implements ServicioAdicionalService {

    private final ServicioAdicionalRepository repo;

    @Override
    public ServicioAdicional save(ServicioAdicional servicio) {
        if (servicio.getIdServicio() != null) {
            throw new IllegalArgumentException("Para actualizar use el método update");
        }
        if (repo.existsByNombre(servicio.getNombre())) {
            throw new IllegalArgumentException("Ya existe un servicio con el nombre: " + servicio.getNombre());
        }
        return repo.save(servicio);
    }

    @Override
    public ServicioAdicional update(ServicioAdicional servicio) {
        if (servicio.getIdServicio() == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo para actualizar");
        }
        ServicioAdicional existente = repo.findById(servicio.getIdServicio())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con ID: " + servicio.getIdServicio()));
        existente.setNombre(servicio.getNombre());
        existente.setDescripcion(servicio.getDescripcion());
        existente.setPrecioUnitario(servicio.getPrecioUnitario());
        existente.setEstaDisponible(servicio.getEstaDisponible());
        return repo.save(existente);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new RuntimeException("Servicio no encontrado con ID: " + id);
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServicioAdicional> findAll() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ServicioAdicional> findById(Long id) {
        if (id == null || id <= 0) throw new IllegalArgumentException("El ID debe ser positivo");
        return repo.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ServicioAdicional> findByNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("El nombre no puede estar vacío");
        return repo.findByNombre(nombre.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServicioAdicional> findByNombreContaining(String nombre) {
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("El nombre no puede estar vacío");
        return repo.findByNombreContainingIgnoreCase(nombre.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServicioAdicional> findByEstaDisponible(Boolean disponible) {
        if (disponible == null) throw new IllegalArgumentException("El parámetro disponible no puede ser nulo");
        return repo.findByEstaDisponible(disponible);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServicioAdicional> findByPrecioMaximo(Double precio) {
        if (precio < 0) throw new IllegalArgumentException("El precio no puede ser negativo");
        return repo.findByPrecioUnitarioLessThanEqual(precio);
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