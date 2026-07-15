package pe.edu.utp.sistemadereservacionhotel.service.finanzas.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sistemadereservacionhotel.dto.request.DetalleComprobanteRequestDTO;
import pe.edu.utp.sistemadereservacionhotel.dto.response.DetalleComprobanteResponseDTO;
import pe.edu.utp.sistemadereservacionhotel.model.finanzas.ComprobantePago;
import pe.edu.utp.sistemadereservacionhotel.model.finanzas.DetalleComprobante;
import pe.edu.utp.sistemadereservacionhotel.repository.finanzas.ComprobantePagoRepository;
import pe.edu.utp.sistemadereservacionhotel.repository.finanzas.DetalleComprobanteRepository;
import pe.edu.utp.sistemadereservacionhotel.service.finanzas.DetalleComprobanteService;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.RecursoNoEncontradoException;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DetalleComprobanteServiceImpl implements DetalleComprobanteService {

    private final DetalleComprobanteRepository detalleRepo;
    private final ComprobantePagoRepository comprobanteRepo;

    @Override
    @Transactional
    public DetalleComprobanteResponseDTO agregarDetalle(DetalleComprobanteRequestDTO request) {
        ComprobantePago comprobante = comprobanteRepo.findById(request.idComprobante())
                .orElseThrow(() -> new RecursoNoEncontradoException("ComprobantePago", request.idComprobante()));

        DetalleComprobante entidad = new DetalleComprobante();
        entidad.setCantidad(request.cantidad());
        entidad.setDescripcion(request.descripcion());
        entidad.setPrecioUnitario(request.precioUnitario());

        // Multiplicación segura con BigDecimal
        BigDecimal subtotal = request.precioUnitario().multiply(new BigDecimal(request.cantidad()));
        entidad.setSubtotalLinea(subtotal);
        entidad.setComprobantePago(comprobante);

        return mapearADto(detalleRepo.save(entidad));
    }

    @Override
    @Transactional
    public DetalleComprobanteResponseDTO actualizarDetalle(Long id, DetalleComprobanteRequestDTO request) {
        DetalleComprobante existente = detalleRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("DetalleComprobante", id));

        existente.setCantidad(request.cantidad());
        existente.setDescripcion(request.descripcion());
        existente.setPrecioUnitario(request.precioUnitario());

        BigDecimal subtotal = request.precioUnitario().multiply(new BigDecimal(request.cantidad()));
        existente.setSubtotalLinea(subtotal);

        return mapearADto(detalleRepo.save(existente));
    }

    @Override
    @Transactional
    public void eliminarDetalle(Long id) {
        if (!detalleRepo.existsById(id)) throw new RecursoNoEncontradoException("DetalleComprobante", id);
        detalleRepo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleComprobanteResponseDTO> listarTodos() {
        return detalleRepo.findAll().stream().map(this::mapearADto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DetalleComprobanteResponseDTO buscarPorId(Long id) {
        return mapearADto(detalleRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("DetalleComprobante", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleComprobanteResponseDTO> buscarPorComprobante(Long idComprobante) {
        return detalleRepo.findByComprobantePago_IdComprobante(idComprobante).stream()
                .map(this::mapearADto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleComprobanteResponseDTO> buscarPorDescripcion(String descripcion) {
        return detalleRepo.findByDescripcionContainingIgnoreCase(descripcion).stream()
                .map(this::mapearADto).collect(Collectors.toList());
    }

    private DetalleComprobanteResponseDTO mapearADto(DetalleComprobante entidad) {
        return new DetalleComprobanteResponseDTO(
                entidad.getIdDetalle(),
                entidad.getCantidad(),
                entidad.getDescripcion(),
                entidad.getPrecioUnitario(),
                entidad.getSubtotalLinea(),
                entidad.getComprobantePago().getIdComprobante()
        );
    }
}