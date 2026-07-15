package pe.edu.utp.sistemadereservacionhotel.repository.reserva;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.Acompanante;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.Parentesco;

import java.util.List;

/**
 * Repositorio para la gestión de los acompañantes vinculados a un contrato de alojamiento.
 */
@Repository
public interface AcompananteRepository extends JpaRepository<Acompanante, Long> {

    /**
     * Recupera el listado completo de acompañantes registrados bajo una misma reserva.
     */
    List<Acompanante> findByReserva_IdReserva(Long idReserva);

    /**
     * Busca el historial de un acompañante basándose en su documento oficial de identidad.
     */
    List<Acompanante> findByDocumentoIdentidad(String documentoIdentidad);

    /**
     * Búsqueda parcial (LIKE) por nombre completo, ignorando mayúsculas y minúsculas.
     */
    List<Acompanante> findByNombreCompletoContainingIgnoreCase(String nombre);

    /**
     * Filtra a los acompañantes basándose en su vínculo con el titular.
     * CAMBIO CRÍTICO: Refactorizado de String a Enum para alinearse con la integridad del dominio
     * y prevenir una excepción fatal en el despliegue del contexto de Spring.
     */
    List<Acompanante> findByParentesco(Parentesco parentesco);
}