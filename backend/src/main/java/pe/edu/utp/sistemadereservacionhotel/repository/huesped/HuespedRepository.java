package pe.edu.utp.sistemadereservacionhotel.repository.huesped;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.huesped.Huesped;

import java.util.List;
import java.util.Optional;

/**
 * Capa de persistencia (Data Access Object) para la entidad Huesped.
 * Abstrae la complejidad de las consultas SQL delegando la implementación a Spring Data JPA.
 */
@Repository
public interface HuespedRepository extends JpaRepository<Huesped, Long> {

    /**
     * Recupera un registro de huésped mediante su correo electrónico exacto.
     * * @param email Correo electrónico normalizado.
     * @return Un Optional que contiene la entidad si se encuentra, o vacío en caso contrario.
     */
    Optional<Huesped> findByEmail(String email);

    /**
     * Recupera un huésped utilizando su identificador legal único.
     * Esencial para las operaciones de validación cruzada y facturación.
     * * @param documentoIdentidad DNI, Pasaporte o NIE del huésped.
     * @return Un Optional con la entidad correspondiente.
     */
    Optional<Huesped> findByDocumentoIdentidad(String documentoIdentidad);

    /**
     * Ejecuta una consulta de coincidencia parcial (LIKE %nombre%) a nivel de motor de base de datos,
     * ignorando diferencias entre mayúsculas y minúsculas para motores case-sensitive.
     * * @param nombre Cadena de texto a buscar.
     * @return Lista de huéspedes que coinciden con el criterio.
     */
    List<Huesped> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Ejecuta una consulta de coincidencia parcial (LIKE %apellidos%) optimizada para búsquedas de recepción.
     * * @param apellidos Cadena de texto a buscar.
     * @return Lista de huéspedes que coinciden con el criterio.
     */
    List<Huesped> findByApellidosContainingIgnoreCase(String apellidos);

    /**
     * Consulta optimizada (Fail-Fast) que retorna un booleano sin extraer la entidad completa.
     * Crítico para las validaciones de negocio previas a la inserción para evitar bloqueos por ConstraintViolation.
     * * @param email Correo a verificar.
     * @return true si el registro existe, false en caso contrario.
     */
    boolean existsByEmail(String email);

    /**
     * Verificación de preexistencia de un documento legal sin cargar el objeto en el contexto de persistencia.
     * * @param documentoIdentidad Documento a verificar.
     * @return true si el documento ya está en el sistema.
     */
    boolean existsByDocumentoIdentidad(String documentoIdentidad);

    /**
     * Filtra el catálogo de clientes omitiendo aquellos que han sufrido una baja lógica.
     * Obligatorio para cumplir con la integridad referencial financiera y las normativas
     * de protección de datos europeas (RGPD), evitando exponer perfiles inactivos.
     * * @return Lista de huéspedes actualmente operativos en el sistema.
     */
    List<Huesped> findByEstadoActivoTrue();
}