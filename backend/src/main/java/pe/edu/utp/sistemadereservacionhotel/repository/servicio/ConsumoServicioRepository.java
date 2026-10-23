package pe.edu.utp.sistemadereservacionhotel.repository.servicio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.servicio.ConsumoServicio;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio transaccional para auditar los cargos extra generados por un huésped.
 */
@Repository
public interface ConsumoServicioRepository extends JpaRepository<ConsumoServicio, Long> {

    /**
     * Recupera todas las líneas de consumo asociadas a la cuenta maestra de una reserva.
     * Indispensable para el cálculo del comprobante final en el Check-Out.
     */
    List<ConsumoServicio> findByReserva_IdReserva(Long idReserva);

    /**
     * Consulta cuántas veces ha sido facturado un servicio específico (Estadísticas operativas).
     */
    List<ConsumoServicio> findByServicioAdicional_IdServicio(Long idServicio);

    /**
     * Extrae los consumos generados dentro de una ventana de tiempo.
     */
    List<ConsumoServicio> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);
}