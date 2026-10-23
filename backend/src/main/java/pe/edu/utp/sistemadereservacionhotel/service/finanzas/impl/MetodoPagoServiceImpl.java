package pe.edu.utp.sistemadereservacionhotel.service.finanzas.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sistemadereservacionhotel.dto.finanzas.MetodoPagoDTO;
import pe.edu.utp.sistemadereservacionhotel.model.finanzas.MetodoPago;
import pe.edu.utp.sistemadereservacionhotel.repository.finanzas.MetodoPagoRepository;
import pe.edu.utp.sistemadereservacionhotel.service.finanzas.MetodoPagoService;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.DuplicadoException;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.RecursoNoEncontradoException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio paramétrico de solo lectura y mantenimiento de catálogos (CRUD de dominio).
 */
@RequiredArgsConstructor
@Service
public class MetodoPagoServiceImpl implements MetodoPagoService {

    private final MetodoPagoRepository repo;

    @Override
    @Transactional
    public MetodoPagoDTO registrarMetodoPago(MetodoPagoDTO dto) {
        if (repo.existsByNombreMetodo(dto.nombreMetodo())) {
            throw new DuplicadoException("Ya existe el método de pago: " + dto.nombreMetodo());
        }

        MetodoPago entidad = new MetodoPago();
        entidad.setNombreMetodo(dto.nombreMetodo());
        entidad.setEsDigital(dto.esDigital());

        return mapearADto(repo.save(entidad));
    }

    @Override
    @Transactional
    public MetodoPagoDTO actualizarMetodoPago(Long id, MetodoPagoDTO dto) {
        MetodoPago existente = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("MétodoPago", id));

        if (!existente.getNombreMetodo().equals(dto.nombreMetodo()) &&
                repo.existsByNombreMetodo(dto.nombreMetodo())) {
            throw new DuplicadoException("Ya existe otro método de pago con ese nombre.");
        }

        existente.setNombreMetodo(dto.nombreMetodo());
        existente.setEsDigital(dto.esDigital());

        return mapearADto(repo.save(existente));
    }

    @Override
    @Transactional
    public void eliminarMetodoPago(Long id) {
        if (!repo.existsById(id)) {
            throw new RecursoNoEncontradoException("MétodoPago", id);
        }
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MetodoPagoDTO> listarTodos() {
        return repo.findAll().stream().map(this::mapearADto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public MetodoPagoDTO buscarPorId(Long id) {
        return mapearADto(repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("MétodoPago", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public MetodoPagoDTO buscarPorNombre(String nombre) {
        return mapearADto(repo.findByNombreMetodo(nombre.trim())
                .orElseThrow(() -> new RecursoNoEncontradoException("Método no encontrado: " + nombre)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MetodoPagoDTO> buscarPorEsDigital(Boolean esDigital) {
        return repo.findByEsDigital(esDigital).stream()
                .map(this::mapearADto).collect(Collectors.toList());
    }

    private MetodoPagoDTO mapearADto(MetodoPago entidad) {
        return new MetodoPagoDTO(
                entidad.getIdMetodo(),
                entidad.getNombreMetodo(),
                entidad.getEsDigital()
        );
    }
}