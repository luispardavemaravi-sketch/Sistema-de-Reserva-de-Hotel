package pe.edu.utp.sistemadereservacionhotel.repository.finanzas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.finanzas.Caja;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio para la gestión de persistencia de las sesiones de Caja.
 * Permite la auditoría de aperturas, cierres y la trazabilidad por empleado.
 */
@Repository
public interface CajaRepository extends JpaRepository<Caja, Long> {

    /**
     * Recupera todas las cajas filtradas por su estado operativo.
     */
    List<Caja> findByEstaAbierta(Boolean estaAbierta);

    /**
     * Recupera el historial de cajas gestionadas por un empleado específico.
     */
    List<Caja> findByEmpleado_IdEmpleado(Long idEmpleado);

    /**
     * Recupera las cajas operadas dentro de un rango de fechas para auditoría contable.
     */
    List<Caja> findByFechaBetween(LocalDate inicio, LocalDate fin);

    /**
     * Recupera todas las cajas que se encuentran actualmente abiertas.
     * Nota técnica: Refactorizado de Optional a List para prevenir NonUniqueResultException
     * en escenarios de múltiples terminales de recepción operando en paralelo.
     */
    List<Caja> findByEstaAbiertaTrue();
}