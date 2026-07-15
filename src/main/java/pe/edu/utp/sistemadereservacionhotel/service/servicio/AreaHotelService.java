package pe.edu.utp.sistemadereservacionhotel.service.servicio;

import pe.edu.utp.sistemadereservacionhotel.dto.servicio.AreaHotelDTO;
import java.util.List;

/**
 * Contrato de servicio para la gestión operativa de áreas del hotel.
 * Orquesta la lógica de negocio y garantiza la integridad de los datos
 * mediante el uso de objetos de transferencia (DTO).
 */
public interface AreaHotelService {

    /**
     * Registra una nueva área en el sistema.
     * @param dto Datos del área a crear.
     * @return El DTO registrado con su ID generado.
     */
    AreaHotelDTO registrarArea(AreaHotelDTO dto);

    /**
     * Actualiza la información de un área existente.
     */
    AreaHotelDTO actualizarArea(Long id, AreaHotelDTO dto);

    /**
     * Elimina un área por su identificador.
     */
    void eliminarArea(Long id);

    /**
     * Retorna el listado completo de áreas registradas.
     */
    List<AreaHotelDTO> listarTodas();

    /**
     * Busca un área específica por su identificador.
     */
    AreaHotelDTO buscarPorId(Long id);

    /**
     * Busca áreas cuya ubicación coincida parcialmente.
     */
    List<AreaHotelDTO> buscarPorUbicacion(String ubicacion);
}