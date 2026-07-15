package pe.edu.utp.sistemadereservacionhotel.service.reserva.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sistemadereservacionhotel.dto.request.CheckInRequestDTO;
import pe.edu.utp.sistemadereservacionhotel.dto.response.CheckInResponseDTO;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.CheckIn;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.Reserva;
import pe.edu.utp.sistemadereservacionhotel.repository.reserva.CheckInRepository;
import pe.edu.utp.sistemadereservacionhotel.repository.reserva.ReservaRepository;
import pe.edu.utp.sistemadereservacionhotel.service.reserva.CheckInService;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.DuplicadoException;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.RecursoNoEncontradoException;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.ValidacionException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación transaccional para el registro de ingresos operativos al hotel.
 */
@RequiredArgsConstructor
@Service
public class CheckInServiceImpl implements CheckInService {

    private final CheckInRepository repo;
    private final ReservaRepository reservaRepo;
    private static final String ENTIDAD = "CheckIn";

    @Override
    @Transactional
    public CheckInResponseDTO realizarCheckIn(CheckInRequestDTO dto) {
        Reserva reserva = reservaRepo.findById(dto.idReserva())
                .orElseThrow(() -> new RecursoNoEncontradoException("Reserva", dto.idReserva()));

        if (repo.findByReserva_IdReserva(reserva.getIdReserva()).isPresent()) {
            throw new DuplicadoException("Ya existe un CheckIn registrado para la reserva: " + reserva.getIdReserva());
        }

        CheckIn entidad = new CheckIn();
        entidad.setReserva(reserva);
        entidad.setFechaHoraRealEntrada(LocalDateTime.now(ZoneId.of("America/Lima")));
        entidad.setObservaciones(dto.observaciones());

        return mapearADto(repo.save(entidad));
    }

    @Override
    @Transactional
    public CheckInResponseDTO actualizarCheckIn(Long id, CheckInRequestDTO dto) {
        CheckIn existente = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(ENTIDAD, id));

        existente.setObservaciones(dto.observaciones());

        return mapearADto(repo.save(existente));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CheckInResponseDTO> listarTodos() {
        return repo.findAll().stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CheckInResponseDTO buscarPorId(Long id) {
        return mapearADto(repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(ENTIDAD, id)));
    }

    @Override
    @Transactional(readOnly = true)
    public CheckInResponseDTO buscarPorReserva(Long idReserva) {
        return mapearADto(repo.findByReserva_IdReserva(idReserva)
                .orElseThrow(() -> new RecursoNoEncontradoException("CheckIn vinculado a la reserva", idReserva)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CheckInResponseDTO> buscarPorRangoFecha(LocalDateTime inicio, LocalDateTime fin) {
        if (inicio.isAfter(fin)) {
            throw new ValidacionException("La fecha inicio no puede ser posterior a la fecha fin");
        }
        return repo.findByFechaHoraRealEntradaBetween(inicio, fin).stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    private CheckInResponseDTO mapearADto(CheckIn e) {
        return new CheckInResponseDTO(
                e.getIdCheckIn(),
                e.getReserva().getIdReserva(),
                e.getFechaHoraRealEntrada(),
                e.getObservaciones()
        );
    }
}