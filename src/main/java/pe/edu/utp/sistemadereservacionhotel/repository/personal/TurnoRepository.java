package pe.edu.utp.sistemadereservacionhotel.repository.personal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.personal.Turno;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface TurnoRepository extends JpaRepository<Turno, Long> {
    List<Turno> findByDiasSemanaContainingIgnoreCase(String dia);

    List<Turno> findByHoraInicioLessThanEqualAndHoraFinalGreaterThanEqual(LocalTime hora, LocalTime hora2);
}