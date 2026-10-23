package pe.edu.utp.sistemadereservacionhotel.service.personal;

import pe.edu.utp.sistemadereservacionhotel.dto.personal.EmpleadoRequestDTO;
import pe.edu.utp.sistemadereservacionhotel.dto.personal.EmpleadoResponseDTO;
import pe.edu.utp.sistemadereservacionhotel.model.personal.CargoEmpleado;

import java.util.List;

/**
 * Contrato de operaciones de negocio (API Interna) para la gestión del Capital Humano (RRHH).
 * Desacopla la capa de presentación REST de la capa de persistencia JPA, garantizando que
 * todas las transacciones de personal pasen por validaciones de dominio estrictas.
 */
public interface EmpleadoService {

    /**
     * Orquesta el alta de un nuevo colaborador en el sistema.
     * Ejecuta validaciones Fail-Fast para evitar colisiones de credenciales únicas (Email)
     * y garantiza la asignación íntegra de una malla horaria operativa (Turno).
     *
     * @param dto Carga útil inmutable proveniente de la capa de presentación.
     * @return El registro consolidado y enmascarado (sin datos sensibles de BD) del empleado creado.
     */
    EmpleadoResponseDTO registrarEmpleado(EmpleadoRequestDTO dto);

    /**
     * Aplica mutaciones sobre el perfil de un colaborador activo.
     * Impide atómicamente la usurpación de correos corporativos ya asignados a terceros.
     *
     * @param id Identificador único primario del empleado a modificar.
     * @param dto Nuevo bloque de datos contractuales.
     * @return El perfil actualizado reflejando los cambios aplicados en la base de datos.
     */
    EmpleadoResponseDTO actualizarEmpleado(Long id, EmpleadoRequestDTO dto);

    /**
     * Inhabilita operativamente a un empleado cesado mediante la aplicación de un borrado lógico.
     * Conserva el rastro del registro físico intacto para no vulnerar la integridad referencial
     * de cajas, turnos, y facturas firmadas históricamente por este colaborador.
     *
     * @param id Identificador del empleado a desactivar.
     */
    void darDeBajaEmpleado(Long id);

    /**
     * Extrae el directorio corporativo omitiendo rigurosamente al personal que ha sido dado de baja.
     *
     * @return Catálogo global de empleados actualmente en funciones.
     */
    List<EmpleadoResponseDTO> listarActivos();

    EmpleadoResponseDTO buscarPorId(Long id);

    EmpleadoResponseDTO buscarPorEmail(String email);

    List<EmpleadoResponseDTO> buscarPorNombre(String nombre);

    List<EmpleadoResponseDTO> buscarPorApellido(String apellido);

    /**
     * Segmenta el directorio corporativo utilizando enumeraciones tipadas estáticamente
     * para mitigar errores de digitación en las búsquedas por rol jerárquico.
     *
     * @param cargo Instancia del Enum CargoEmpleado (ej. RECEPCIONISTA, GERENTE).
     */
    List<EmpleadoResponseDTO> buscarPorCargo(CargoEmpleado cargo);

    List<EmpleadoResponseDTO> buscarPorCiudad(String ciudad);
}