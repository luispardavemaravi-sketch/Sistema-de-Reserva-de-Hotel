package pe.edu.utp.sistemadereservacionhotel.service.servicio;

import pe.edu.utp.sistemadereservacionhotel.dto.servicio.ServicioAdicionalDTO;
import java.util.List;

/**
 * Contrato de servicio para la gestión del inventario de servicios adicionales.
 * Este servicio se encarga de la administración de las definiciones de servicios,
 * excluyendo la gestión de precios, la cual se delega a CatalogoServicioService.
 */
public interface ServicioAdicionalService {

    /**
     * Registra un nuevo servicio en el inventario.
     * @param dto Datos del servicio a registrar.
     * @return El DTO registrado con su identificador generado.
     */
    ServicioAdicionalDTO registrarServicio(ServicioAdicionalDTO dto);

    /**
     * Actualiza la información operativa de un servicio existente.
     */
    ServicioAdicionalDTO actualizarServicio(Long id, ServicioAdicionalDTO dto);

    /**
     * Elimina un servicio del inventario mediante su identificador.
     */
    void eliminarServicio(Long id);

    /**
     * Retorna el listado completo de servicios definidos.
     */
    List<ServicioAdicionalDTO> listarTodos();

    /**
     * Busca un servicio por su identificador único.
     */
    ServicioAdicionalDTO buscarPorId(Long id);

    /**
     * Busca un servicio exacto por su nombre comercial.
     */
    ServicioAdicionalDTO buscarPorNombre(String nombre);

    /**
     * Busca servicios cuyo nombre contenga la cadena proporcionada.
     */
    List<ServicioAdicionalDTO> buscarPorNombreContiene(String nombre);

    /**
     * Filtra los servicios según su estado actual de disponibilidad.
     */
    List<ServicioAdicionalDTO> buscarPorDisponibilidad(Boolean disponible);

    /**
     * Verifica si un servicio ya existe en el sistema para prevenir duplicados.
     */
    boolean existePorNombre(String nombre);
}