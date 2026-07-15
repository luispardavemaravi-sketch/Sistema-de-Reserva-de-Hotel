package pe.edu.utp.sistemadereservacionhotel.repository.finanzas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.finanzas.MetodoPago;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio de catálogo para la gestión de los medios de pago aceptados.
 */
@Repository
public interface MetodoPagoRepository extends JpaRepository<MetodoPago, Long> {

    /**
     * Busca un método de pago por su denominación exacta.
     */
    Optional<MetodoPago> findByNombreMetodo(String nombreMetodo);

    /**
     * Verifica la existencia de un método de pago.
     */
    boolean existsByNombreMetodo(String nombreMetodo);

    /**
     * Filtra los métodos de pago según su naturaleza (digital o física).
     */
    List<MetodoPago> findByEsDigital(Boolean esDigital);
}