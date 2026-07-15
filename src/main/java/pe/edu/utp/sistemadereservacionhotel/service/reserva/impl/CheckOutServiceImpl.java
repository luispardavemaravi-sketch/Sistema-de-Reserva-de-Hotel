package pe.edu.utp.sistemadereservacionhotel.service.reserva.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sistemadereservacionhotel.dto.reserva.CheckOutRequestDTO;
import pe.edu.utp.sistemadereservacionhotel.dto.reserva.CheckOutResponseDTO;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.CheckOut;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.Reserva;
import pe.edu.utp.sistemadereservacionhotel.repository.reserva.CheckOutRepository;
import pe.edu.utp.sistemadereservacionhotel.repository.reserva.ReservaRepository;
import pe.edu.utp.sistemadereservacionhotel.service.reserva.CheckOutService;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.DuplicadoException;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.RecursoNoEncontradoException;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.ValidacionException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación transaccional para la gestión de salidas del hotel y auditoría de multas.
 */
@RequiredArgsConstructor
@Service
public class CheckOutServiceImpl implements CheckOutService {

    private final CheckOutRepository repo;
    private final ReservaRepository reservaRepo;
    private static final String ENTIDAD = "CheckOut";

    @Override
    @Transactional
    public CheckOutResponseDTO realizarCheckOut(CheckOutRequestDTO dto) {
        Reserva reserva = reservaRepo.findById(dto.idReserva())
                .orElseThrow(() -> new RecursoNoEncontradoException("Reserva", dto.idReserva()));

        if (repo.findByReserva_IdReserva(reserva.getIdReserva()).isPresent()) {
            throw new DuplicadoException("Ya existe un registro de CheckOut para la reserva: " + reserva.getIdReserva());
        }

        CheckOut entidad = new CheckOut();
        entidad.setReserva(reserva);
        entidad.setFechaHoraRealSalida(LocalDateTime.now(ZoneId.of("America/Lima")));
        entidad.setMultaPorRetraso(dto.multa()); // BigDecimal, no Double

        return mapearADto(repo.save(entidad));
    }

    @Override
    @Transactional
    public CheckOutResponseDTO actualizarCheckOut(Long id, CheckOutRequestDTO dto) {
        CheckOut existente = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(ENTIDAD, id));

        existente.setMultaPorRetraso(dto.multa());

        return mapearADto(repo.save(existente));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CheckOutResponseDTO> listarTodos() {
        return repo.findAll().stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CheckOutResponseDTO buscarPorId(Long id) {
        return mapearADto(repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(ENTIDAD, id)));
    }

    @Override
    @Transactional(readOnly = true)
    public CheckOutResponseDTO buscarPorReserva(Long idReserva) {
        return mapearADto(repo.findByReserva_IdReserva(idReserva)
                .orElseThrow(() -> new RecursoNoEncontradoException("CheckOut de Reserva", idReserva)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CheckOutResponseDTO> buscarPorRangoFecha(LocalDateTime inicio, LocalDateTime fin) {
        if (inicio.isAfter(fin)) {
            throw new ValidacionException("La fecha inicio no puede ser posterior a la fecha fin");
        }
        return repo.findByFechaHoraRealSalidaBetween(inicio, fin).stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CheckOutResponseDTO> buscarConMulta(BigDecimal montoMinimo) {
        if (montoMinimo == null || montoMinimo.signum() < 0) {
            throw new ValidacionException("El monto mínimo no puede ser negativo");
        }
        return repo.findByMultaPorRetrasoGreaterThan(montoMinimo).stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    private CheckOutResponseDTO mapearADto(CheckOut entidad) {
        return new CheckOutResponseDTO(
                entidad.getIdCheckOut(),
                entidad.getReserva().getIdReserva(),
                entidad.getFechaHoraRealSalida(),
                entidad.getMultaPorRetraso(),
                entidad.getObservaciones()
        );
    }
}