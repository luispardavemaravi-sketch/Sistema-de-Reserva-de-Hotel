package pe.edu.utp.sistemadereservacionhotel.service.personal;

import pe.edu.utp.sistemadereservacionhotel.model.personal.Turno;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface TurnoService {
    Turno save(Turno turno);

    Turno update(Turno turno);

    void delete(Long id);

    List<Turno> findAll();

    Optional<Turno> findById(Long id);

    List<Turno> findByDia(String dia);

    List<Turno> findByHora(LocalTime hora);

    long count();
}