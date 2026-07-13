package pe.edu.utp.sistemadereservacionhotel.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sistemadereservacionhotel.model.servicio.AreaHotel;
import pe.edu.utp.sistemadereservacionhotel.repository.servicio.AreaHotelRepository;
import pe.edu.utp.sistemadereservacionhotel.service.servicio.AreaHotelService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class AreaHotelServiceImpl implements AreaHotelService {

    private final AreaHotelRepository repo;

    @Override
    public AreaHotel save(AreaHotel areaHotel) {
        if (areaHotel.getIdAreaHotel() != null) {
            throw new IllegalArgumentException("Para actualizar use el método update");
        }
        if (repo.existsByNombreArea(areaHotel.getNombreArea())) {
            throw new IllegalArgumentException("Ya existe un área con el nombre: " + areaHotel.getNombreArea());
        }
        return repo.save(areaHotel);
    }

    @Override
    public AreaHotel update(AreaHotel areaHotel) {
        if (areaHotel.getIdAreaHotel() == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo para actualizar");
        }
        AreaHotel existente = repo.findById(areaHotel.getIdAreaHotel())
                .orElseThrow(() -> new RuntimeException("Área no encontrada con ID: " + areaHotel.getIdAreaHotel()));
        existente.setNombreArea(areaHotel.getNombreArea());
        existente.setUbicacion(areaHotel.getUbicacion());
        return repo.save(existente);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new RuntimeException("Área no encontrada con ID: " + id);
        repo.deleteById(id);
    }

    @Override @Transactional(readOnly = true)
    public List<AreaHotel> findAll() { return repo.findAll(); }

    @Override @Transactional(readOnly = true)
    public Optional<AreaHotel> findById(Long id) {
        if (id == null || id <= 0) throw new IllegalArgumentException("El ID debe ser positivo");
        return repo.findById(id);
    }

    @Override @Transactional(readOnly = true)
    public Optional<AreaHotel> findByNombreArea(String nombre) {
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("El nombre no puede estar vacío");
        return repo.findByNombreArea(nombre.trim());
    }

    @Override @Transactional(readOnly = true)
    public List<AreaHotel> findByNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("El nombre no puede estar vacío");
        return repo.findByNombreAreaContainingIgnoreCase(nombre.trim());
    }

    @Override @Transactional(readOnly = true)
    public List<AreaHotel> findByUbicacion(String ubicacion) {
        if (ubicacion == null || ubicacion.isBlank()) throw new IllegalArgumentException("La ubicación no puede estar vacía");
        return repo.findByUbicacionContainingIgnoreCase(ubicacion.trim());
    }

    @Override @Transactional(readOnly = true)
    public boolean existsByNombreArea(String nombre) { return repo.existsByNombreArea(nombre.trim()); }

    @Override @Transactional(readOnly = true)
    public long count() { return repo.count(); }
}