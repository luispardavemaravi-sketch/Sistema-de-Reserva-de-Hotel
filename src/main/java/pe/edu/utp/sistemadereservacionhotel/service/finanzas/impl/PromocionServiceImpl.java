package pe.edu.utp.sistemadereservacionhotel.service.finanzas.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sistemadereservacionhotel.dto.finanzas.PromocionDTO;
import pe.edu.utp.sistemadereservacionhotel.model.finanzas.Promocion;
import pe.edu.utp.sistemadereservacionhotel.repository.finanzas.PromocionRepository;
import pe.edu.utp.sistemadereservacionhotel.service.finanzas.PromocionService;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.DuplicadoException;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.RecursoNoEncontradoException;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.ValidacionException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio encargado de orquestar la habilitación y control temporal de campañas (cupones).
 */
@RequiredArgsConstructor
@Service
public class PromocionServiceImpl implements PromocionService {

    private final PromocionRepository repo;

    /**
     * Da de alta un cupón promocional, aplicando validaciones preventivas sobre las fechas de caducidad.
     */
    @Override
    @Transactional
    public PromocionDTO registrarPromocion(PromocionDTO dto) {
        if (repo.existsByCodigoCupon(dto.codigoCupon())) {
            throw new DuplicadoException("Ya existe una promoción con el código: " + dto.codigoCupon());
        }
        if (dto.fechaCaducidad().isBefore(LocalDate.now(ZoneId.of("America/Lima")))) {
            throw new ValidacionException("La fecha de caducidad no puede ser anterior a hoy");
        }

        Promocion entidad = new Promocion();
        entidad.setCodigoCupon(dto.codigoCupon().toUpperCase());
        entidad.setPorcentajeDescuento(dto.porcentajeDescuento());
        entidad.setFechaCaducidad(dto.fechaCaducidad());

        return mapearADto(repo.save(entidad));
    }

    @Override
    @Transactional
    public PromocionDTO actualizarPromocion(Long id, PromocionDTO dto) {
        Promocion existente = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Promocion", id));

        if (!existente.getCodigoCupon().equalsIgnoreCase(dto.codigoCupon()) &&
                repo.existsByCodigoCupon(dto.codigoCupon())) {
            throw new DuplicadoException("Ya existe otro cupón con ese código.");
        }

        existente.setCodigoCupon(dto.codigoCupon().toUpperCase());
        existente.setPorcentajeDescuento(dto.porcentajeDescuento());
        existente.setFechaCaducidad(dto.fechaCaducidad());

        return mapearADto(repo.save(existente));
    }

    @Override
    @Transactional
    public void eliminarPromocion(Long id) {
        if (!repo.existsById(id)) throw new RecursoNoEncontradoException("Promocion", id);
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromocionDTO> listarTodos() {
        return repo.findAll().stream().map(this::mapearADto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PromocionDTO buscarPorId(Long id) {
        return mapearADto(repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Promocion", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public PromocionDTO buscarPorCodigoCupon(String codigo) {
        return mapearADto(repo.findByCodigoCupon(codigo.trim().toUpperCase())
                .orElseThrow(() -> new RecursoNoEncontradoException("Cupón no encontrado: " + codigo)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromocionDTO> buscarVigentes() {
        return repo.findByFechaCaducidadAfter(LocalDate.now(ZoneId.of("America/Lima"))).stream()
                .map(this::mapearADto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromocionDTO> buscarPorPorcentajeMinimo(BigDecimal porcentaje) {
        return repo.findByPorcentajeDescuentoGreaterThanEqual(porcentaje).stream()
                .map(this::mapearADto).collect(Collectors.toList());
    }

    private PromocionDTO mapearADto(Promocion entidad) {
        return new PromocionDTO(
                entidad.getIdPromocion(),
                entidad.getCodigoCupon(),
                entidad.getPorcentajeDescuento(),
                entidad.getFechaCaducidad()
        );
    }
}