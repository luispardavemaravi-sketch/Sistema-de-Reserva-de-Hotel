package pe.edu.utp.sistemadereservacionhotel.service.habitacion.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sistemadereservacionhotel.dto.habitacion.PrecioHabitacionDTO;
import pe.edu.utp.sistemadereservacionhotel.model.habitacion.Habitacion;
import pe.edu.utp.sistemadereservacionhotel.model.habitacion.PrecioHabitacion;
import pe.edu.utp.sistemadereservacionhotel.repository.habitacion.HabitacionRepository;
import pe.edu.utp.sistemadereservacionhotel.repository.habitacion.PrecioHabitacionRepository;
import pe.edu.utp.sistemadereservacionhotel.service.habitacion.PrecioHabitacionService;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.RecursoNoEncontradoException;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.ValidacionException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio maestro para la gestión del historial de tarifas dinámicas.
 * Desacopla la fluctuación de precios de la entidad Habitación para permitir reportes financieros históricos.
 */
@RequiredArgsConstructor
@Service
public class PrecioHabitacionServiceImpl implements PrecioHabitacionService {

    private final PrecioHabitacionRepository precioRepo;
    private final HabitacionRepository habitacionRepo;

    /**
     * Fija un nuevo bloque tarifario para una habitación física específica.
     * Valida la coherencia cronológica de la vigencia del precio.
     */
    @Override
    @Transactional
    public PrecioHabitacionDTO establecerTarifa(PrecioHabitacionDTO dto) {
        if (dto.fechaInicio().isAfter(dto.fechaFin())) {
            throw new ValidacionException("La fecha de inicio no puede ser posterior a la fecha fin");
        }

        Habitacion habitacion = habitacionRepo.findById(dto.idHabitacion())
                .orElseThrow(() -> new RecursoNoEncontradoException("Habitacion", dto.idHabitacion()));

        PrecioHabitacion entidad = new PrecioHabitacion();
        entidad.setMonto(dto.monto());
        entidad.setFechaInicio(dto.fechaInicio());
        entidad.setFechaFin(dto.fechaFin());
        entidad.setHabitacion(habitacion);

        return mapearADto(precioRepo.save(entidad));
    }

    /**
     * Extrae la trazabilidad de los cambios de precio aplicados históricamente a un mismo cuarto.
     */
    @Override
    @Transactional(readOnly = true)
    public List<PrecioHabitacionDTO> listarHistorialPorHabitacion(Long idHabitacion) {
        return precioRepo.findByHabitacion_IdHabitacion(idHabitacion).stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrecioHabitacionDTO> buscarPorRangoFecha(LocalDate inicio, LocalDate fin) {
        return precioRepo.findByFechaInicioBetween(inicio, fin).stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    /**
     * Búsqueda paramétrica asegurando el uso de BigDecimal para evitar fugas de precisión en coma flotante.
     */
    @Override
    @Transactional(readOnly = true)
    public List<PrecioHabitacionDTO> buscarPorRangoMonto(BigDecimal min, BigDecimal max) {
        if (min.compareTo(max) > 0) {
            throw new ValidacionException("El monto mínimo no puede ser mayor al máximo");
        }
        return precioRepo.findByMontoBetween(min, max).stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    private PrecioHabitacionDTO mapearADto(PrecioHabitacion entidad) {
        return new PrecioHabitacionDTO(
                entidad.getIdPrecio(),
                entidad.getHabitacion().getIdHabitacion(),
                entidad.getMonto(),
                entidad.getFechaInicio(),
                entidad.getFechaFin()
        );
    }
}