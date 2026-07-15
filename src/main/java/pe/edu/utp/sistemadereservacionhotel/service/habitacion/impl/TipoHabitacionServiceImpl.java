package pe.edu.utp.sistemadereservacionhotel.service.habitacion.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sistemadereservacionhotel.dto.habitacion.TipoHabitacionDTO;
import pe.edu.utp.sistemadereservacionhotel.model.habitacion.TipoHabitacion;
import pe.edu.utp.sistemadereservacionhotel.repository.habitacion.TipoHabitacionRepository;
import pe.edu.utp.sistemadereservacionhotel.service.habitacion.TipoHabitacionService;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.DuplicadoException;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.RecursoNoEncontradoException;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.ValidacionException;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio paramétrico responsable del catálogo de categorías comerciales del hotel (ej. Suite, Doble, Presidencial).
 * Define las restricciones base como el aforo máximo y el precio piso para el motor de reservas.
 */
@Service
@RequiredArgsConstructor
public class TipoHabitacionServiceImpl implements TipoHabitacionService {

    private final TipoHabitacionRepository repo;

    /**
     * Fija un nuevo nivel de clasificación en el portafolio del hotel.
     */
    @Override
    @Transactional
    public TipoHabitacionDTO registrarTipo(TipoHabitacionDTO dto) {
        if (repo.existsByNombre(dto.nombre())) {
            throw new DuplicadoException("Ya existe un tipo con el nombre: " + dto.nombre());
        }

        TipoHabitacion entidad = new TipoHabitacion();
        entidad.setNombre(dto.nombre());
        entidad.setCapacidadMaxima(dto.capacidadMaxima());
        entidad.setPrecioBase(dto.precioBase());

        return mapearADto(repo.save(entidad));
    }

    /**
     * Ajusta la estructura de una categoría, implementando bloqueos Fail-Fast
     * para evitar superposición de nomenclaturas comerciales.
     */
    @Override
    @Transactional
    public TipoHabitacionDTO actualizarTipo(Long id, TipoHabitacionDTO dto) {
        TipoHabitacion existente = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("TipoHabitacion", id));

        if (!existente.getNombre().equalsIgnoreCase(dto.nombre()) &&
                repo.existsByNombre(dto.nombre())) {
            throw new DuplicadoException("Ya existe otro tipo de habitación con ese nombre.");
        }

        existente.setNombre(dto.nombre());
        existente.setCapacidadMaxima(dto.capacidadMaxima());
        existente.setPrecioBase(dto.precioBase());

        return mapearADto(repo.save(existente));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoHabitacionDTO> listarTodos() {
        return repo.findAll().stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TipoHabitacionDTO buscarPorId(Long id) {
        return mapearADto(repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("TipoHabitacion", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoHabitacionDTO> buscarPorRangoPrecio(BigDecimal precioMin, BigDecimal precioMax) {
        if (precioMin.compareTo(precioMax) > 0) {
            throw new ValidacionException("El precio mínimo no puede ser mayor al precio máximo");
        }
        return repo.findByPrecioBaseBetween(precioMin, precioMax).stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    private TipoHabitacionDTO mapearADto(TipoHabitacion entidad) {
        return new TipoHabitacionDTO(
                entidad.getIdTipo(),
                entidad.getNombre(),
                entidad.getCapacidadMaxima(),
                entidad.getPrecioBase()
        );
    }
}