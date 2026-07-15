package pe.edu.utp.sistemadereservacionhotel.repository.habitacion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.habitacion.PrecioHabitacion;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio para auditar y consultar el historial de fluctuación de tarifas.
 */
@Repository
public interface PrecioHabitacionRepository extends JpaRepository<PrecioHabitacion, Long> {

    /**
     * Recupera todo el historial de cambios de precio de una habitación específica.
     */
    List<PrecioHabitacion> findByHabitacion_IdHabitacion(Long idHabitacion);

    /**
     * Filtra los precios cuya vigencia comenzó dentro de un rango de fechas determinado.
     */
    List<PrecioHabitacion> findByFechaInicioBetween(LocalDate inicio, LocalDate fin);

    /**
     * Localiza registros históricos de tarifas que fluctuaron dentro de un rango monetario.
     * CAMBIO CRÍTICO: Refactorizado de Double a BigDecimal.
     */
    List<PrecioHabitacion> findByMontoBetween(BigDecimal montoMin, BigDecimal montoMax);
}