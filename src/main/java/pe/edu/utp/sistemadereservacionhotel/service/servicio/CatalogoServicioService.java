package pe.edu.utp.sistemadereservacionhotel.service.servicio;

import pe.edu.utp.sistemadereservacionhotel.model.servicio.CatalogoServicio;

import java.util.List;
import java.util.Optional;

public interface CatalogoServicioService {
    CatalogoServicio save(CatalogoServicio catalogo);

    CatalogoServicio update(CatalogoServicio catalogo);

    void delete(Long id);

    List<CatalogoServicio> findAll();

    Optional<CatalogoServicio> findById(Long id);

    List<CatalogoServicio> findByCategoria(String categoria);

    List<CatalogoServicio> findByServicio(Long idServicio);

    List<CatalogoServicio> findByPrecioMaximo(Double precio);

    long count();
}