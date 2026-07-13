package pe.edu.utp.sistemadereservacionhotel.service.reserva.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.Acompanante;
import pe.edu.utp.sistemadereservacionhotel.repository.reserva.AcompananteRepository;
import pe.edu.utp.sistemadereservacionhotel.service.reserva.AcompananteService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class AcompananteServiceImpl implements AcompananteService {

    private final AcompananteRepository repo;

    @Override
    public Acompanante save(Acompanante acompanante) {
        if (acompanante.getIdAcompanante() != null) {
            throw new IllegalArgumentException("Para actualizar use el método update");
        }
        return repo.save(acompanante);
    }

    @Override
    public Acompanante update(Acompanante acompanante) {
        if (acompanante.getIdAcompanante() == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo para actualizar");
        }
        Acompanante existente = repo.findById(acompanante.getIdAcompanante())
                .orElseThrow(() -> new RuntimeException("Acompañante no encontrado con ID: " + acompanante.getIdAcompanante()));

        existente.setNombreCompleto(acompanante.getNombreCompleto());
        existente.setDocumentoIdentidad(acompanante.getDocumentoIdentidad());
        existente.setParentesco(acompanante.getParentesco());

        return repo.save(existente);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Acompañante no encontrado con ID: " + id);
        }
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Acompanante> findAll() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Acompanante> findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo");
        }
        return repo.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Acompanante> findByReserva(Long idReserva) {
        return repo.findByReserva_IdReserva(idReserva);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Acompanante> findByDocumentoIdentidad(String documento) {
        if (documento == null || documento.isBlank()) {
            throw new IllegalArgumentException("El documento no puede estar vacío");
        }
        return repo.findByDocumentoIdentidad(documento.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Acompanante> findByNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        return repo.findByNombreCompletoContainingIgnoreCase(nombre.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Acompanante> findByParentesco(String parentesco) {
        if (parentesco == null || parentesco.isBlank()) {
            throw new IllegalArgumentException("El parentesco no puede estar vacío");
        }
        return repo.findByParentesco(parentesco.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return repo.count();
    }
}