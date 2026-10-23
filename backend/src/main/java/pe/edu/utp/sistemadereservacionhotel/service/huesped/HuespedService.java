package pe.edu.utp.sistemadereservacionhotel.service.huesped;

import pe.edu.utp.sistemadereservacionhotel.dto.huesped.HuespedRequestDTO;
import pe.edu.utp.sistemadereservacionhotel.dto.huesped.HuespedResponseDTO;

import java.util.List;

/**
 * Contrato de operaciones de negocio para la gestión de huéspedes.
 * Centraliza la validación de identidad y el control de acceso al perfil del cliente.
 */
public interface HuespedService {

    /**
     * Da de alta un nuevo huésped en el sistema.
     * Válida que el email y documento no estén duplicados antes de la inserción.
     */
    HuespedResponseDTO registrarHuesped(HuespedRequestDTO dto);

    /**
     * Actualiza los datos modificables de contacto del huésped.
     * Protege la inmutabilidad del documento de identidad y del correo electrónico.
     */
    HuespedResponseDTO actualizarHuesped(Long id, HuespedRequestDTO dto);

    /**
     * Ejecuta la baja lógica del cliente para cumplir con la integridad transaccional
     * de los históricos de reserva y con las normativas europeas de protección de datos (RGPD).
     */
    void darDeBajaHuesped(Long id);

    List<HuespedResponseDTO> listarTodos();

    HuespedResponseDTO buscarPorId(Long id);

    HuespedResponseDTO buscarPorEmail(String email);

    HuespedResponseDTO buscarPorDocumentoIdentidad(String documento);

    List<HuespedResponseDTO> buscarPorNombre(String nombre);

    List<HuespedResponseDTO> buscarPorApellidos(String apellidos);
}