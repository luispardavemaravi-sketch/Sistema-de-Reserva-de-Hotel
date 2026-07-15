package pe.edu.utp.sistemadereservacionhotel.repository.huesped;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.huesped.Huesped;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio maestro para la gestión de clientes (huéspedes).
 * Centraliza las consultas de identidad y contacto para el motor de reservas.
 */
@Repository
public interface HuespedRepository extends JpaRepository<Huesped, Long> {

    /**
     * Localiza a un huésped mediante su correo electrónico exacto.
     */
    Optional<Huesped> findByEmail(String email);

    /**
     * Valida si un correo electrónico ya se encuentra registrado (evita duplicidad de cuentas).
     */
    boolean existsByEmail(String email);

    /**
     * Busca a un huésped utilizando su documento de identidad oficial.
     */
    Optional<Huesped> findByDocumentoIdentidad(String documentoIdentidad);

    /**
     * Verifica la existencia de un documento de identidad previo a la creación del perfil.
     */
    boolean existsByDocumentoIdentidad(String documentoIdentidad);

    /**
     * Búsqueda parcial por nombre de pila, ignorando sensibilidad a mayúsculas.
     */
    List<Huesped> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Búsqueda parcial por apellidos, ignorando sensibilidad a mayúsculas.
     */
    List<Huesped> findByApellidosContainingIgnoreCase(String apellidos);

    /**
     * CAMBIO CRÍTICO: Recupera únicamente los huéspedes que no han sido eliminados lógicamente.
     * Obligatorio para cumplir con la integridad de datos frente a normativas como el RGPD.
     */
    List<Huesped> findByEstadoActivoTrue();
}