package pe.edu.utp.sistemadereservacionhotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.EstadoReserva;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstadoReservaRepository extends JpaRepository<EstadoReserva, Long> {

    Optional<EstadoReserva> findByNombreEstado(String nombreEstado);

    boolean existsByNombreEstado(String nombreEstado);

    List<EstadoReserva> findByEsModificable(Boolean esModificable);
}