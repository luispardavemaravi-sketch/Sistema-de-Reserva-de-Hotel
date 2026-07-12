package pe.edu.utp.sistemadereservacionhotel.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.EstadoReserva;
import pe.edu.utp.sistemadereservacionhotel.repository.reserva.EstadoReservaRepository;
import pe.edu.utp.sistemadereservacionhotel.service.reserva.EstadoReservaService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class EstadoReservaServiceImpl implements EstadoReservaService {

    private final EstadoReservaRepository repo;

    @Override
    public EstadoReserva save(EstadoReserva estadoReserva) {
        if (estadoReserva.getIdEstado() != null) {
            throw new IllegalArgumentException("Para actualizar use el método update");
        }
        if (repo.existsByNombreEstado(estadoReserva.getNombreEstado())) {
            throw new IllegalArgumentException("Ya existe un estado con el nombre: " + estadoReserva.getNombreEstado());
        }
        return repo.save(estadoReserva);
    }

    @Override
    public EstadoReserva update(EstadoReserva estadoReserva) {
        if (estadoReserva.getIdEstado() == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo para actualizar");
        }
        EstadoReserva existente = repo.findById(estadoReserva.getIdEstado())
                .orElseThrow(() -> new RuntimeException("Estado no encontrado con ID: " + estadoReserva.getIdEstado()));

        existente.setNombreEstado(estadoReserva.getNombreEstado());
        existente.setEsModificable(estadoReserva.getEsModificable());

        return repo.save(existente);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Estado no encontrado con ID: " + id);
        }
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EstadoReserva> findAll() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EstadoReserva> findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo");
        }
        return repo.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EstadoReserva> findByNombreEstado(String nombreEstado) {
        if (nombreEstado == null || nombreEstado.isBlank()) {
            throw new IllegalArgumentException("El nombre del estado no puede estar vacío");
        }
        return repo.findByNombreEstado(nombreEstado.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EstadoReserva> findByEsModificable(Boolean esModificable) {
        if (esModificable == null) {
            throw new IllegalArgumentException("El parámetro esModificable no puede ser nulo");
        }
        return repo.findByEsModificable(esModificable);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNombreEstado(String nombreEstado) {
        return repo.existsByNombreEstado(nombreEstado);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return repo.count();
    }
}