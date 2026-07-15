package pe.edu.utp.sistemadereservacionhotel.service.finanzas.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sistemadereservacionhotel.model.finanzas.Impuesto;
import pe.edu.utp.sistemadereservacionhotel.repository.finanzas.ImpuestoRepository;
import pe.edu.utp.sistemadereservacionhotel.service.finanzas.ImpuestoService;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.DuplicadoException;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.RecursoNoEncontradoException;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.ValidacionException;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class ImpuestoServiceImpl implements ImpuestoService {

    private final ImpuestoRepository repo;

    @Override
    public Impuesto save(Impuesto impuesto) {
        if (impuesto.getIdImpuesto() != null)
            throw new ValidacionException("Para actualizar use el método update");
        if (repo.existsByNombreImpuesto(impuesto.getNombreImpuesto()))
            throw new DuplicadoException("Ya existe un impuesto con el nombre: " + impuesto.getNombreImpuesto());
        return repo.save(impuesto);
    }

    @Override
    public Impuesto update(Impuesto impuesto) {
        if (impuesto.getIdImpuesto() == null)
            throw new ValidacionException("El ID no puede ser nulo para actualizar");
        Impuesto existente = repo.findById(impuesto.getIdImpuesto())
                .orElseThrow(() -> new RecursoNoEncontradoException("Impuesto", impuesto.getIdImpuesto()));
        existente.setNombreImpuesto(impuesto.getNombreImpuesto());
        existente.setPorcentaje(impuesto.getPorcentaje());
        return repo.save(existente);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new RecursoNoEncontradoException("Impuesto", id);
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Impuesto> findAll() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Impuesto> findById(Long id) {
        if (id == null || id <= 0)
            throw new ValidacionException("El ID debe ser positivo");
        return repo.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Impuesto> findByNombre(String nombre) {
        if (nombre == null || nombre.isBlank())
            throw new ValidacionException("El nombre no puede estar vacío");
        return repo.findByNombreImpuesto(nombre.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Impuesto> findByPorcentajeMaximo(Double porcentaje) {
        return repo.findByPorcentajeLessThanEqual(porcentaje);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNombre(String nombre) {
        return repo.existsByNombreImpuesto(nombre.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return repo.count();
    }
}