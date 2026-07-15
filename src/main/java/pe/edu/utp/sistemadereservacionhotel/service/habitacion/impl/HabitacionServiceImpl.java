package pe.edu.utp.sistemadereservacionhotel.service.habitacion.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sistemadereservacionhotel.dto.habitacion.HabitacionRequestDTO;
import pe.edu.utp.sistemadereservacionhotel.dto.habitacion.HabitacionResponseDTO;
import pe.edu.utp.sistemadereservacionhotel.model.habitacion.Habitacion;
import pe.edu.utp.sistemadereservacionhotel.model.habitacion.Piso;
import pe.edu.utp.sistemadereservacionhotel.model.habitacion.TipoHabitacion;
import pe.edu.utp.sistemadereservacionhotel.repository.habitacion.HabitacionRepository;
import pe.edu.utp.sistemadereservacionhotel.repository.habitacion.PisoRepository;
import pe.edu.utp.sistemadereservacionhotel.repository.habitacion.TipoHabitacionRepository;
import pe.edu.utp.sistemadereservacionhotel.service.habitacion.HabitacionService;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.DuplicadoException;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.RecursoNoEncontradoException;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.ValidacionException;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HabitacionServiceImpl implements HabitacionService {

    private final HabitacionRepository habitacionRepo;
    private final PisoRepository pisoRepo;
    private final TipoHabitacionRepository tipoHabitacionRepo;

    @Override
    @Transactional
    public HabitacionResponseDTO registrarHabitacion(HabitacionRequestDTO dto) {
        if (habitacionRepo.existsByNumeroHabitacion(dto.numeroHabitacion())) {
            throw new DuplicadoException("Ya existe una habitación con el número: " + dto.numeroHabitacion());
        }

        Piso piso = pisoRepo.findById(dto.idPiso())
                .orElseThrow(() -> new RecursoNoEncontradoException("Piso", dto.idPiso()));

        TipoHabitacion tipo = tipoHabitacionRepo.findById(dto.idTipoHabitacion())
                .orElseThrow(() -> new RecursoNoEncontradoException("TipoHabitacion", dto.idTipoHabitacion()));

        Habitacion entidad = new Habitacion();
        entidad.setNumeroHabitacion(dto.numeroHabitacion());
        entidad.setPrecioActual(dto.precioActual());
        entidad.setPiso(piso);
        entidad.setTipoHabitacion(tipo);
        entidad.setEstadoActivo(true); // Estado lógico por defecto al crear

        Habitacion guardada = habitacionRepo.save(entidad);
        return mapearADto(guardada);
    }

    @Override
    @Transactional
    public HabitacionResponseDTO actualizarHabitacion(Long id, HabitacionRequestDTO dto) {
        Habitacion existente = habitacionRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Habitacion", id));

        if (!existente.getNumeroHabitacion().equalsIgnoreCase(dto.numeroHabitacion()) &&
                habitacionRepo.existsByNumeroHabitacion(dto.numeroHabitacion())) {
            throw new DuplicadoException("Ya existe otra habitación con el número: " + dto.numeroHabitacion());
        }

        Piso piso = pisoRepo.findById(dto.idPiso())
                .orElseThrow(() -> new RecursoNoEncontradoException("Piso", dto.idPiso()));

        TipoHabitacion tipo = tipoHabitacionRepo.findById(dto.idTipoHabitacion())
                .orElseThrow(() -> new RecursoNoEncontradoException("TipoHabitacion", dto.idTipoHabitacion()));

        existente.setNumeroHabitacion(dto.numeroHabitacion());
        existente.setPrecioActual(dto.precioActual());
        existente.setPiso(piso);
        existente.setTipoHabitacion(tipo);

        Habitacion actualizada = habitacionRepo.save(existente);
        return mapearADto(actualizada);
    }

    @Override
    @Transactional
    public void darDeBajaHabitacion(Long id) {
        Habitacion existente = habitacionRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Habitacion", id));

        // Borrado lógico: no se elimina de la BD, solo se inactiva
        existente.setEstadoActivo(false);
        habitacionRepo.save(existente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HabitacionResponseDTO> listarTodas() {
        return habitacionRepo.findAll().stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public HabitacionResponseDTO buscarPorId(Long id) {
        return mapearADto(habitacionRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Habitacion", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public HabitacionResponseDTO buscarPorNumero(String numero) {
        return mapearADto(habitacionRepo.findByNumeroHabitacion(numero.trim())
                .orElseThrow(() -> new RecursoNoEncontradoException("Habitación no encontrada con número: " + numero)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<HabitacionResponseDTO> buscarPorRangoPrecio(BigDecimal min, BigDecimal max) {
        if (min.compareTo(max) > 0) {
            throw new ValidacionException("El precio mínimo no puede ser mayor al precio máximo");
        }
        return habitacionRepo.findByPrecioActualBetween(min, max).stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public HabitacionResponseDTO actualizarTarifa(Long id, BigDecimal nuevoPrecio) {
        if (nuevoPrecio == null || nuevoPrecio.signum() < 0) {
            throw new ValidacionException("El precio no puede ser nulo o negativo");
        }

        Habitacion hab = habitacionRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Habitacion", id));

        hab.setPrecioActual(nuevoPrecio);
        Habitacion actualizada = habitacionRepo.save(hab);

        return mapearADto(actualizada);
    }

    /**
     * Mapeo manual estandarizado para aislar la entidad de la capa de presentación.
     */
    private HabitacionResponseDTO mapearADto(Habitacion entidad) {
        return new HabitacionResponseDTO(
                entidad.getIdHabitacion(),
                entidad.getNumeroHabitacion(),
                entidad.getTipoHabitacion().getNombre(),
                entidad.getPrecioActual(),
                entidad.isEstadoActivo()
        );
    }
}