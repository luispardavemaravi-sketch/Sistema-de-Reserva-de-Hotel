package pe.edu.utp.sistemadereservacionhotel.repository.servicio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.servicio.CatalogoServicio;
import pe.edu.utp.sistemadereservacionhotel.model.servicio.CategoriaServicio;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repositorio central de la lógica financiera de servicios (Motor de Precios).
 */
@Repository
public interface CatalogoServicioRepository extends JpaRepository<CatalogoServicio, Long> {

    /**
     * Filtra el catálogo en base a la categoría estructurada del servicio.
     * CAMBIO CRÍTICO: Refactorizado de String al Enum CategoriaServicio para evitar colapso de JPA.
     */
    List<CatalogoServicio> findByCategoria(CategoriaServicio categoria);

    /**
     * Recupera el historial de precios configurados para un servicio base específico.
     */
    List<CatalogoServicio> findByServicioAdicional_IdServicio(Long idServicio);

    /**
     * Filtra tarifas vigentes que se encuentren por debajo de un tope presupuestal.
     * CAMBIO CRÍTICO: Refactorizado de Double a BigDecimal para coincidir con la entidad.
     */
    List<CatalogoServicio> findByPrecioVigenteLessThanEqual(BigDecimal precio);
}