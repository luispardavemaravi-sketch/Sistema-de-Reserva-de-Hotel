package pe.edu.utp.sistemadereservacionhotel.service.reserva.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sistemadereservacionhotel.dto.EstadoReservaDTO;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.EstadoReserva;
import pe.edu.utp.sistemadereservacionhotel.repository.reserva.EstadoReservaRepository;
import pe.edu.utp.sistemadereservacionhotel.service.reserva.EstadoReservaService;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.DuplicadoException;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.RecursoNoEncontradoException;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.ValidacionException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio encargado de gestionar los estados del ciclo de vida de las reservas.
 * Implementa el patrón de aislamiento de capa mediante DTO.
 */
@RequiredArgsConstructor
@Service
public class EstadoReservaServiceImpl implements EstadoReservaService {

    private final EstadoReservaRepository repo;
    private static final String ENTIDAD = "Estado de Reserva";

    @Override
    @Transactional
    public EstadoReservaDTO registrarEstado(EstadoReservaDTO dto) {
        if (repo.existsByNombreEstado(dto.nombreEstado())) {
            throw new DuplicadoException("Ya existe un estado con el nombre: " + dto.nombreEstado());
        }
        EstadoReserva entidad = new EstadoReserva();
        entidad.setNombreEstado(dto.nombreEstado().trim());
        entidad.setEsModificable(dto.esModificable());

        return mapearADto(repo.save(entidad));
    }

    @Override
    @Transactional
    public EstadoReservaDTO actualizarEstado(Long id, EstadoReservaDTO dto) {
        EstadoReserva existente = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(ENTIDAD, id));

        existente.setNombreEstado(dto.nombreEstado().trim());
        existente.setEsModificable(dto.esModificable());

        return mapearADto(repo.save(existente));
    }

    @Override
    @Transactional
    public void eliminarEstado(Long id) {
        if (!repo.existsById(id)) {
            throw new RecursoNoEncontradoException(ENTIDAD, id);
        }
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EstadoReservaDTO> listarTodos() {
        return repo.findAll().stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EstadoReservaDTO buscarPorId(Long id) {
        return mapearADto(repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(ENTIDAD, id)));
    }

    @Override
    @Transactional(readOnly = true)
    public EstadoReservaDTO buscarPorNombre(String nombreEstado) {
        return mapearADto(repo.findByNombreEstado(nombreEstado.trim())
                .orElseThrow(() -> new RecursoNoEncontradoException(ENTIDAD + " con nombre: " + nombreEstado, 0L)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EstadoReservaDTO> buscarPorEsModificable(Boolean esModificable) {
        if (esModificable == null) {
            throw new ValidacionException("El parámetro esModificable es obligatorio");
        }
        return repo.findByEsModificable(esModificable).stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    private EstadoReservaDTO mapearADto(EstadoReserva e) {
        return new EstadoReservaDTO(e.getIdEstado(), e.getNombreEstado(), e.getEsModificable());
    }
}