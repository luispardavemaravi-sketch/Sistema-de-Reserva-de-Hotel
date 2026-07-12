package pe.edu.utp.sistemadereservacionhotel.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sistemadereservacionhotel.model.huesped.Huesped;
import pe.edu.utp.sistemadereservacionhotel.repository.huesped.HuespedRepository;
import pe.edu.utp.sistemadereservacionhotel.service.huesped.HuespedService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class HuespedServiceImpl implements HuespedService {
    private final HuespedRepository repo;

    @Override
    public Huesped save(Huesped huesped) {
        if (huesped.getIdHuesped() != null) {
            throw new IllegalArgumentException("Para actualizar use el método update");
        }
        if (repo.existsByEmail(huesped.getEmail().trim().toLowerCase())) {
            throw new IllegalArgumentException("Ya existe un huésped con el email: " + huesped.getEmail());
        }
        if (repo.existsByDocumentoIdentidad(huesped.getDocumentoIdentidad())) {
            throw new IllegalArgumentException("Ya existe un huésped con el documento: " + huesped.getDocumentoIdentidad());
        }
        return repo.save(huesped);
    }

    @Override
    public Huesped update(Huesped huesped) {
        if (huesped.getIdHuesped() == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo para actualizar");
        }
        Huesped existente = repo.findById(huesped.getIdHuesped())
                .orElseThrow(() -> new RuntimeException("Huésped no encontrado con ID: " + huesped.getIdHuesped()));

        existente.setNombre(huesped.getNombre());
        existente.setApellidos(huesped.getApellidos());
        existente.setTelefono(huesped.getTelefono());
        // email y documento de identidad no se actualizan por seguridad/integridad

        return repo.save(existente);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Huésped no encontrado con ID: " + id);
        }
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Huesped> findAll() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Huesped> findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo");
        }
        return repo.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Huesped> findByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }
        return repo.findByEmail(email.trim().toLowerCase());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Huesped> findByDocumentoIdentidad(String documento) {
        if (documento == null || documento.isBlank()) {
            throw new IllegalArgumentException("El documento no puede estar vacío");
        }
        return repo.findByDocumentoIdentidad(documento.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Huesped> findByNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        return repo.findByNombreContainingIgnoreCase(nombre.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Huesped> findByApellidos(String apellidos) {
        if (apellidos == null || apellidos.isBlank()) {
            throw new IllegalArgumentException("Los apellidos no pueden estar vacíos");
        }
        return repo.findByApellidosContainingIgnoreCase(apellidos.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return repo.existsByEmail(email.trim().toLowerCase());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByDocumentoIdentidad(String documento) {
        return repo.existsByDocumentoIdentidad(documento.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return repo.count();
    }
}
