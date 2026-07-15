package pe.edu.utp.sistemadereservacionhotel.repository.personal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.personal.CargoEmpleado;
import pe.edu.utp.sistemadereservacionhotel.model.personal.Empleado;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz Data Access Object (DAO) para la persistencia del personal corporativo.
 * Extiende de JpaRepository para heredar las operaciones transaccionales base de Hibernate.
 * * ATENCIÓN: Todas las consultas de extracción masiva han sido sufijadas con 'AndEstadoActivoTrue'
 * para garantizar a nivel de motor SQL que nunca se expongan perfiles de empleados inactivos
 * hacia capas superiores, cumpliendo así con las directrices europeas (RGPD) de minimización de datos.
 */
@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    /**
     * Intenta recuperar las credenciales de un empleado garantizando que la cuenta no esté suspendida.
     * Fundamental para mecanismos futuros de autenticación y autorización (Spring Security).
     *
     * @param email Correo electrónico corporativo o personal registrado.
     * @return Optional encapsulando la entidad si el empleado existe y posee un contrato activo.
     */
    Optional<Empleado> findByEmailAndEstadoActivoTrue(String email);

    /**
     * Validación de bloqueo ultrarrápida. Retorna un flag booleano directamente desde el motor
     * de base de datos sin ejecutar la deserialization del objeto JPA completo.
     *
     * @param email Cadena de texto a evaluar contra el constraint Unique.
     */
    boolean existsByEmail(String email);

    /**
     * @return Catálogo global base filtrado estáticamente por la cláusula WHERE estado_activo = true.
     */
    List<Empleado> findByEstadoActivoTrue();

    List<Empleado> findByNombreContainingIgnoreCaseAndEstadoActivoTrue(String nombre);

    List<Empleado> findByApellidoContainingIgnoreCaseAndEstadoActivoTrue(String apellido);

    /**
     * Búsqueda analítica para agrupar planillas por nivel jerárquico.
     * Spring Data traduce automáticamente el Enum a su representación de cadena (VARCHAR) en el esquema SQL.
     */
    List<Empleado> findByCargoAndEstadoActivoTrue(CargoEmpleado cargo);

    List<Empleado> findByCiudadContainingIgnoreCaseAndEstadoActivoTrue(String ciudad);
}