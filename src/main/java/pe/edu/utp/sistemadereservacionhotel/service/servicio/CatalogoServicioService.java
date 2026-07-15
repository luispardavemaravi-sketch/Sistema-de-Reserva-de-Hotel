package pe.edu.utp.sistemadereservacionhotel.service.servicio;

import pe.edu.utp.sistemadereservacionhotel.dto.servicio.CatalogoServicioDTO;
import java.math.BigDecimal;
import java.util.List;

/**
 * Contrato de negocio para la gestión del catálogo de servicios adicionales.
 * Orquesta la lógica de precios y categorización para la facturación.
 */
public interface CatalogoServicioService {

    /**
     * Registra un nuevo servicio en el catálogo.
     * @param dto Datos del servicio a catalogar.
     * @return El DTO persistido con su ID.
     */
    CatalogoServicioDTO registrarEnCatalogo(CatalogoServicioDTO dto);

    /**
     * Actualiza los datos financieros o categóricos de un ítem del catálogo.
     */
    CatalogoServicioDTO actualizarEnCatalogo(Long id, CatalogoServicioDTO dto);

    /**
     * Retira un servicio del catálogo activo.
     */
    void eliminarDelCatalogo(Long id);

    /**
     * Lista todos los servicios disponibles en el catálogo.
     */
    List<CatalogoServicioDTO> listarTodos();

    /**
     * Busca un ítem del catálogo por su identificador único.
     */
    CatalogoServicioDTO buscarPorId(Long id);

    /**
     * Filtra servicios por su categoría comercial.
     */
    List<CatalogoServicioDTO> buscarPorCategoria(String categoria);

    /**
     * Filtra servicios según un límite de precio máximo para reportes de presupuesto.
     */
    List<CatalogoServicioDTO> buscarPorPrecioMaximo(BigDecimal precioMaximo);
}