package pe.edu.utp.sistemadereservacionhotel.service.reserva.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sistemadereservacionhotel.dto.reserva.ReservaRequestDTO;
import pe.edu.utp.sistemadereservacionhotel.dto.reserva.ReservaResponseDTO;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.Reserva;
import pe.edu.utp.sistemadereservacionhotel.repository.habitacion.HabitacionRepository;
import pe.edu.utp.sistemadereservacionhotel.repository.huesped.HuespedRepository;
import pe.edu.utp.sistemadereservacionhotel.repository.reserva.*;
import pe.edu.utp.sistemadereservacionhotel.service.reserva.ReservaService;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReservaServiceImpl implements ReservaService {

    private final ReservaRepository repo;
    private final HuespedRepository huespedRepo;
    private final HabitacionRepository habitacionRepo;
    private final EstadoReservaRepository estadoRepo;
    private static final String ENTIDAD = "Reserva";

    @Override
    @Transactional
    public ReservaResponseDTO crearReserva(ReservaRequestDTO dto) {
        // 1. FAIL-FAST: Validación en memoria antes de golpear la base de datos
        if (dto.fechaSalidaPlanificada().isBefore(dto.fechaEntradaPlanificada())) {
            throw new IllegalArgumentException("La fecha de salida planificada no puede ser anterior a la entrada.");
        }

        // 2. Ejecución de I/O solo con datos válidos
        var huesped = huespedRepo.findById(dto.idHuesped())
                .orElseThrow(() -> new RecursoNoEncontradoException("Huesped", dto.idHuesped()));

        var habitacion = habitacionRepo.findById(dto.idHabitacion())
                .orElseThrow(() -> new RecursoNoEncontradoException("Habitacion", dto.idHabitacion()));

        // 3. Construcción del objeto
        Reserva reserva = new Reserva();
        reserva.setCodigoReserva(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        reserva.setFechaReserva(LocalDateTime.now(ZoneId.of("America/Lima")));
        reserva.setHuesped(huesped);
        reserva.setHabitacion(habitacion);
        reserva.setFechaEntradaPlanificada(dto.fechaEntradaPlanificada());
        reserva.setFechaSalidaPlanificada(dto.fechaSalidaPlanificada());

        // Alerta de diseño: Hardcoding (1L) es una mala práctica para entornos de producción.
        // Deberías usar una constante o un Enum para buscar el estado inicial (ej. PENDIENTE).
        reserva.setEstadoReserva(estadoRepo.findById(1L)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estado", 1L)));

        // 4. Persistencia y mapeo
        return mapearADto(repo.save(reserva));
    }

    @Override
    @Transactional
    public ReservaResponseDTO actualizarReserva(Long id, ReservaRequestDTO dto) {
        Reserva existente = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(ENTIDAD, id));

        existente.setFechaEntradaPlanificada(dto.fechaEntradaPlanificada());
        existente.setFechaSalidaPlanificada(dto.fechaSalidaPlanificada());

        return mapearADto(repo.save(existente));
    }

    @Override
    @Transactional
    public void cancelarReserva(Long id) {
        Reserva reserva = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(ENTIDAD, id));

        if (reserva.getCheckIn() != null) {
            throw new ValidacionException("No se puede cancelar una reserva que ya inició.");
        }

        reserva.setEstadoReserva(estadoRepo.findById(3L).orElseThrow());
        repo.save(reserva);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservaResponseDTO> listarTodas() {
        return repo.findAll().stream().map(this::mapearADto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ReservaResponseDTO buscarPorId(Long id) {
        return mapearADto(repo.findById(id).orElseThrow(() -> new RecursoNoEncontradoException(ENTIDAD, id)));
    }

    @Override
    @Transactional(readOnly = true)
    public ReservaResponseDTO buscarPorCodigo(String codigoReserva) {
        return mapearADto(repo.findByCodigoReserva(codigoReserva).orElseThrow(() -> new RecursoNoEncontradoException(ENTIDAD, codigoReserva)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservaResponseDTO> buscarPorHuesped(Long idHuesped) {
        return repo.findByHuesped_IdHuesped(idHuesped).stream().map(this::mapearADto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservaResponseDTO> buscarPorEstado(Long idEstado) {
        return repo.findByEstadoReserva_IdEstado(idEstado).stream().map(this::mapearADto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservaResponseDTO> buscarPorFechaEntrada(LocalDate fecha) {
        return repo.findByFechaEntradaPlanificada(fecha).stream().map(this::mapearADto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservaResponseDTO> buscarPorRangoFechas(LocalDate inicio, LocalDate fin) {
        return repo.findByFechaEntradaPlanificadaBetween(inicio, fin).stream().map(this::mapearADto).collect(Collectors.toList());
    }

    private ReservaResponseDTO mapearADto(Reserva r) {
        return new ReservaResponseDTO(
                r.getIdReserva(),
                r.getCodigoReserva(),
                r.getFechaReserva(),
                r.getFechaEntradaPlanificada(),
                r.getFechaSalidaPlanificada(),
                r.getMontoTotalEstimado(),
                r.getHuesped().getIdHuesped(),
                r.getHabitacion().getIdHabitacion(),
                r.getEstadoReserva().getNombreEstado()
        );
    }
}