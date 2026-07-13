package pe.edu.utp.sistemadereservacionhotel.repository.servicio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.servicio.CatalogoServicio;

import java.util.List;

@Repository
public interface CatalogoServicioRepository extends JpaRepository<CatalogoServicio, Long> {
    List<CatalogoServicio> findByCategoria(String categoria);

    List<CatalogoServicio> findByServicioAdicional_IdServicio(Long idServicio);

    List<CatalogoServicio> findByPrecioVigenteLessThanEqual(Double precio);
}