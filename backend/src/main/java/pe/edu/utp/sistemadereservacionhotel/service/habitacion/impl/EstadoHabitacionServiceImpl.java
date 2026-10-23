package pe.edu.utp.sistemadereservacionhotel.service.habitacion.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sistemadereservacionhotel.dto.habitacion.EstadoHabitacionDTO;
import pe.edu.utp.sistemadereservacionhotel.model.habitacion.EstadoHabitacion;
import pe.edu.utp.sistemadereservacionhotel.repository.habitacion.EstadoHabitacionRepository;
import pe.edu.utp.sistemadereservacionhotel.service.habitacion.EstadoHabitacionService;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.RecursoNoEncontradoException;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EstadoHabitacionServiceImpl implements EstadoHabitacionService {

    private final EstadoHabitacionRepository repo;

    @Override
    @Transactional
    public EstadoHabitacionDTO registrar(EstadoHabitacionDTO dto) {
        if (repo.existsByNombreEstado(dto.nombreEstado())) {
            throw new IllegalArgumentException("Ya existe un estado: " + dto.nombreEstado());
        }
        EstadoHabitacion entidad = new EstadoHabitacion();
        entidad.setNombreEstado(dto.nombreEstado());
        return mapearADto(repo.save(entidad));
    }

    @Override
    @Transactional
    public EstadoHabitacionDTO actualizar(Long id, EstadoHabitacionDTO dto) {
        EstadoHabitacion existente = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("EstadoHabitacion", id));

        existente.setNombreEstado(dto.nombreEstado());
        return mapearADto(repo.save(existente));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!repo.existsById(id)) {
            throw new RecursoNoEncontradoException("EstadoHabitacion", id);
        }
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EstadoHabitacionDTO> listarTodos() {
        return repo.findAll().stream().map(this::mapearADto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EstadoHabitacionDTO buscarPorId(Long id) {
        return mapearADto(repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("EstadoHabitacion", id)));
    }


    private EstadoHabitacionDTO mapearADto(EstadoHabitacion e) {
        return new EstadoHabitacionDTO(e.getIdEstado(), e.getNombreEstado());
    }
}