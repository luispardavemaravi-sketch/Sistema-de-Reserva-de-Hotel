package pe.edu.utp.sistemadereservacionhotel.service.servicio.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sistemadereservacionhotel.model.servicio.CatalogoServicio;
import pe.edu.utp.sistemadereservacionhotel.repository.servicio.CatalogoServicioRepository;
import pe.edu.utp.sistemadereservacionhotel.service.servicio.CatalogoServicioService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class CatalogoServicioServiceImpl implements CatalogoServicioService {

    private final CatalogoServicioRepository repo;

    @Override
    public CatalogoServicio save(CatalogoServicio catalogo) {
        if (catalogo.getIdCatalogo() != null) {
            throw new IllegalArgumentException("Para actualizar use el método update");
        }
        return repo.save(catalogo);
    }

    @Override
    public CatalogoServicio update(CatalogoServicio catalogo) {
        if (catalogo.getIdCatalogo() == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo para actualizar");
        }
        CatalogoServicio existente = repo.findById(catalogo.getIdCatalogo())
                .orElseThrow(() -> new RuntimeException("Catálogo no encontrado con ID: " + catalogo.getIdCatalogo()));
        existente.setPrecioVigente(catalogo.getPrecioVigente());
        existente.setCategoria(catalogo.getCategoria());
        existente.setServicioAdicional(catalogo.getServicioAdicional());
        return repo.save(existente);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new RuntimeException("Catálogo no encontrado con ID: " + id);
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CatalogoServicio> findAll() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CatalogoServicio> findById(Long id) {
        if (id == null || id <= 0) throw new IllegalArgumentException("El ID debe ser positivo");
        return repo.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CatalogoServicio> findByCategoria(String categoria) {
        if (categoria == null || categoria.isBlank())
            throw new IllegalArgumentException("La categoría no puede estar vacía");
        return repo.findByCategoria(categoria.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CatalogoServicio> findByServicio(Long idServicio) {
        return repo.findByServicioAdicional_IdServicio(idServicio);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CatalogoServicio> findByPrecioMaximo(Double precio) {
        if (precio < 0) throw new IllegalArgumentException("El precio no puede ser negativo");
        return repo.findByPrecioVigenteLessThanEqual(precio);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return repo.count();
    }
}