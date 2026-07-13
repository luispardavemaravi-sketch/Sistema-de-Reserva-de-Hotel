package pe.edu.utp.sistemadereservacionhotel.repository.servicio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.servicio.ConsumoServicio;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConsumoServicioRepository extends JpaRepository<ConsumoServicio, Long> {
    List<ConsumoServicio> findByReserva_IdReserva(Long idReserva);

    List<ConsumoServicio> findByServicioAdicional_IdServicio(Long idServicio);

    List<ConsumoServicio> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);
}