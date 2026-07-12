package pe.edu.utp.sistemadereservacionhotel.repository.finanzas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.finanzas.Caja;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface CajaRepository extends JpaRepository<Caja, Long> {
    List<Caja> findByEstaAbierta(Boolean estaAbierta);

    List<Caja> findByEmpleado_IdEmpleado(Long idEmpleado);

    List<Caja> findByFechaBetween(LocalDate inicio, LocalDate fin);

    Optional<Caja> findByEstaAbiertaTrue();
}