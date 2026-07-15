package pe.edu.utp.sistemadereservacionhotel.service.finanzas.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sistemadereservacionhotel.dto.request.ComprobanteRequestDTO;
import pe.edu.utp.sistemadereservacionhotel.dto.response.ComprobanteResponseDTO;
import pe.edu.utp.sistemadereservacionhotel.model.finanzas.ComprobantePago;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.Reserva;
import pe.edu.utp.sistemadereservacionhotel.repository.finanzas.ComprobantePagoRepository;
import pe.edu.utp.sistemadereservacionhotel.repository.reserva.ReservaRepository;
import pe.edu.utp.sistemadereservacionhotel.service.finanzas.ComprobantePagoService;
import pe.edu.utp.sistemadereservacionhotel.service.patron.creacional.ComprobanteFactory;
import pe.edu.utp.sistemadereservacionhotel.service.patron.creacional.ComprobanteFactorySelector;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.DuplicadoException;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.RecursoNoEncontradoException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ComprobantePagoServiceImpl implements ComprobantePagoService {

    private final ComprobantePagoRepository comprobanteRepo;
    private final ReservaRepository reservaRepo;

    @Override
    @Transactional
    public ComprobanteResponseDTO emitirComprobante(ComprobanteRequestDTO request) {

        Reserva reserva = reservaRepo.findById(request.idReserva())
                .orElseThrow(() -> new RecursoNoEncontradoException("Reserva", request.idReserva()));

        if (comprobanteRepo.findByReserva_IdReserva(reserva.getIdReserva()).isPresent()) {
            throw new DuplicadoException("La reserva ya tiene un comprobante emitido.");
        }

        // Lógica de fábrica
        ComprobanteFactory factory = ComprobanteFactorySelector.obtenerFactory(request.tipoComprobante());
        ComprobantePago comprobante = factory.crear(reserva, reserva.getMontoTotalEstimado());

        ComprobantePago guardado = comprobanteRepo.save(comprobante);

        return mapearADto(guardado);
    }

    @Override
    @Transactional
    public ComprobanteResponseDTO anularComprobante(Long idComprobante) {
        // En un sistema real, aquí se emitiría una nota de crédito.
        // Por ahora, lanzamos excepción si no existe.
        ComprobantePago existente = comprobanteRepo.findById(idComprobante)
                .orElseThrow(() -> new RecursoNoEncontradoException("ComprobantePago", idComprobante));

        // Lógica de anulación...
        return mapearADto(existente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComprobanteResponseDTO> listarTodos() {
        return comprobanteRepo.findAll().stream().map(this::mapearADto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ComprobanteResponseDTO buscarPorId(Long id) {
        return mapearADto(comprobanteRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("ComprobantePago", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public ComprobanteResponseDTO buscarPorNumeroSerie(String serie) {
        return mapearADto(comprobanteRepo.findByNumeroSerie(serie)
                .orElseThrow(() -> new RecursoNoEncontradoException("Comprobante con serie " + serie + " no encontrado")));
    }

    @Override
    @Transactional(readOnly = true)
    public ComprobanteResponseDTO buscarPorReserva(Long idReserva) {
        return mapearADto(comprobanteRepo.findByReserva_IdReserva(idReserva)
                .orElseThrow(() -> new RecursoNoEncontradoException("No hay comprobante para la reserva " + idReserva)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComprobanteResponseDTO> buscarPorRangoFecha(LocalDateTime inicio, LocalDateTime fin) {
        return comprobanteRepo.findByFechaEmisionBetween(inicio, fin).stream()
                .map(this::mapearADto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComprobanteResponseDTO> buscarPorRangoMonto(BigDecimal min, BigDecimal max) {
        return comprobanteRepo.findByMontoTotalBetween(min, max).stream()
                .map(this::mapearADto).collect(Collectors.toList());
    }

    private ComprobanteResponseDTO mapearADto(ComprobantePago entidad) {
        return new ComprobanteResponseDTO(
                entidad.getIdComprobante(),
                entidad.getNumeroSerie(),
                entidad.getFechaEmision(),
                entidad.getMontoTotal(),
                entidad.getReserva().getIdReserva()
        );
    }
}