package pe.edu.utp.sistemadereservacionhotel.repository.personal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.edu.utp.sistemadereservacionhotel.dto.personal.TurnoDisponibleDTO;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.personal.Turno;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

/**
 * Repositorio transaccional para la gestión de la malla horaria operativa.
 */
@Repository
public interface TurnoRepository extends JpaRepository<Turno, Long> {

    @Query("""
            select new pe.edu.utp.sistemadereservacionhotel.dto.personal.TurnoDisponibleDTO(
                t.idTurno, t.horaInicio, t.horaFinal)
            from Turno t
            where t.horaInicio <> t.horaFinal and t.diasSemana is not empty
            order by t.idTurno
            """)
    List<TurnoDisponibleDTO> findDisponiblesParaLogin();

    /**
     * CAMBIO CRÍTICO: Refactorizado para buscar dentro de la @ElementCollection usando el Enum DayOfWeek.
     * Spring Data JPA iterará sobre la tabla secundaria 'turno_dias' de manera transparente.
     */
    List<Turno> findByDiasSemana(DayOfWeek dia);

    /**
     * Evalúa si un bloque temporal específico intersecta con los horarios del turno.
     * Útil para calcular qué turnos cubren una hora determinada del día.
     */
    List<Turno> findByHoraInicioLessThanEqualAndHoraFinalGreaterThanEqual(LocalTime horaInicio, LocalTime horaFinal);
}
