package pe.edu.utp.sistemadereservacionhotel.service.servicio.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sistemadereservacionhotel.dto.servicio.ConsumoServicioDTO;
import pe.edu.utp.sistemadereservacionhotel.model.servicio.ConsumoServicio;
import pe.edu.utp.sistemadereservacionhotel.repository.reserva.ReservaRepository;
import pe.edu.utp.sistemadereservacionhotel.repository.servicio.*;
import pe.edu.utp.sistemadereservacionhotel.service.servicio.ConsumoServicioService;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.RecursoNoEncontradoException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación de la lógica de negocio para consumos de servicios.
 * Utiliza el Catálogo de Servicios como fuente única de verdad para la valoración económica.
 */
@RequiredArgsConstructor
@Service
public class ConsumoServicioServiceImpl implements ConsumoServicioService {

    private final ConsumoServicioRepository repo;
    private final ReservaRepository reservaRepo;
    private final ServicioAdicionalRepository servicioRepo;
    private final CatalogoServicioRepository catalogoRepo; // Inyectamos el catálogo
    private static final String ENTIDAD = "Consumo de Servicio";

    @Override
    @Transactional
    public ConsumoServicioDTO registrarConsumo(ConsumoServicioDTO dto) {
        var reserva = reservaRepo.findById(dto.idReserva())
                .orElseThrow(() -> new RecursoNoEncontradoException("Reserva", dto.idReserva()));

        var servicio = servicioRepo.findById(dto.idServicio())
                .orElseThrow(() -> new RecursoNoEncontradoException("Servicio", dto.idServicio()));

        // Obtenemos el precio desde el catálogo
        var catalogo = catalogoRepo.findByServicioAdicional_IdServicio(servicio.getIdServicio())
                .stream().findFirst()
                .orElseThrow(() -> new RuntimeException("El servicio no tiene un precio asignado en el catálogo"));

        ConsumoServicio entidad = new ConsumoServicio();
        entidad.setReserva(reserva);
        entidad.setServicioAdicional(servicio);
        entidad.setCantidad(dto.cantidad());
        entidad.setFechaHora(LocalDateTime.now(ZoneId.of("America/Lima")));
        entidad.setSubTotal(catalogo.getPrecioVigente().multiply(BigDecimal.valueOf(dto.cantidad())));

        return mapearADto(repo.save(entidad));
    }

    @Override
    @Transactional
    public ConsumoServicioDTO actualizarConsumo(Long id, ConsumoServicioDTO dto) {
        ConsumoServicio existente = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(ENTIDAD, id));

        // Obtenemos el precio vigente para recalcular
        var catalogo = catalogoRepo.findByServicioAdicional_IdServicio(existente.getServicioAdicional().getIdServicio())
                .stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Precio no encontrado en catálogo"));

        existente.setCantidad(dto.cantidad());
        existente.setSubTotal(catalogo.getPrecioVigente().multiply(BigDecimal.valueOf(dto.cantidad())));

        return mapearADto(repo.save(existente));
    }

    @Override
    @Transactional
    public void eliminarConsumo(Long id) {
        if (!repo.existsById(id)) throw new RecursoNoEncontradoException(ENTIDAD, id);
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConsumoServicioDTO> listarPorReserva(Long idReserva) {
        return repo.findByReserva_IdReserva(idReserva).stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    private ConsumoServicioDTO mapearADto(ConsumoServicio e) {
        return new ConsumoServicioDTO(
                e.getIdConsumo(),
                e.getReserva().getIdReserva(),
                e.getServicioAdicional().getIdServicio(),
                e.getCantidad(),
                e.getSubTotal(),
                e.getFechaHora()
        );
    }
}