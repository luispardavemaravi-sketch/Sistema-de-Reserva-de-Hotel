package pe.edu.utp.sistemadereservacionhotel.service.finanzas.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sistemadereservacionhotel.dto.ImpuestoDTO;
import pe.edu.utp.sistemadereservacionhotel.model.finanzas.Impuesto;
import pe.edu.utp.sistemadereservacionhotel.repository.finanzas.ImpuestoRepository;
import pe.edu.utp.sistemadereservacionhotel.service.finanzas.ImpuestoService;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.DuplicadoException;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.RecursoNoEncontradoException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación transaccional de la lógica de negocio fiscal.
 * Aplica el patrón Fail-Fast y mapeo manual entre el DTO y la Entidad.
 */
@RequiredArgsConstructor
@Service
public class ImpuestoServiceImpl implements ImpuestoService {

    private final ImpuestoRepository repo;

    @Override
    @Transactional
    public ImpuestoDTO registrarImpuesto(ImpuestoDTO dto) {
        if (repo.existsByNombreImpuesto(dto.nombreImpuesto())) {
            throw new DuplicadoException("Ya existe un impuesto con el nombre: " + dto.nombreImpuesto());
        }

        Impuesto entidad = new Impuesto();
        entidad.setNombreImpuesto(dto.nombreImpuesto());
        entidad.setPorcentaje(dto.porcentaje()); // Esta línea ya no arrojará error.

        Impuesto guardado = repo.save(entidad);
        return mapearADto(guardado);
    }

    @Override
    @Transactional
    public ImpuestoDTO actualizarImpuesto(Long id, ImpuestoDTO dto) {
        Impuesto existente = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Impuesto", id));

        if (!existente.getNombreImpuesto().equals(dto.nombreImpuesto()) &&
                repo.existsByNombreImpuesto(dto.nombreImpuesto())) {
            throw new DuplicadoException("Ya existe otro impuesto con el nombre: " + dto.nombreImpuesto());
        }

        existente.setNombreImpuesto(dto.nombreImpuesto());
        existente.setPorcentaje(dto.porcentaje());

        Impuesto actualizado = repo.save(existente);
        return mapearADto(actualizado);
    }

    @Override
    @Transactional(readOnly = true)
    public ImpuestoDTO buscarPorId(Long id) {
        Impuesto impuesto = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Impuesto", id));
        return mapearADto(impuesto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImpuestoDTO> listarTodos() {
        return repo.findAll()
                .stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void eliminarImpuesto(Long id) {
        if (!repo.existsById(id)) {
            throw new RecursoNoEncontradoException("Impuesto", id);
        }
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ImpuestoDTO buscarPorNombre(String nombre) {
        Impuesto impuesto = repo.findByNombreImpuesto(nombre.trim())
                .orElseThrow(() -> new RecursoNoEncontradoException("Impuesto no encontrado con nombre: " + nombre));
        return mapearADto(impuesto);
    }

    private ImpuestoDTO mapearADto(Impuesto entidad) {
        return new ImpuestoDTO(
                entidad.getIdImpuesto(),
                entidad.getNombreImpuesto(),
                entidad.getPorcentaje()
        );
    }
}