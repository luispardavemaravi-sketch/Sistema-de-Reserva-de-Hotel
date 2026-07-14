package pe.edu.utp.sistemadereservacionhotel.service.personal.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sistemadereservacionhotel.model.personal.Turno;
import pe.edu.utp.sistemadereservacionhotel.repository.personal.TurnoRepository;
import pe.edu.utp.sistemadereservacionhotel.service.personal.TurnoService;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class TurnoServiceImpl implements TurnoService {

    private final TurnoRepository repo;

    @Override
    public Turno save(Turno turno) {
        if (turno.getIdTurno() != null) {
            throw new IllegalArgumentException("Para actualizar use el método update");
        }
        if (turno.getHoraInicio().isAfter(turno.getHoraFinal())) {
            throw new IllegalArgumentException("La hora de inicio no puede ser posterior a la hora final");
        }
        return repo.save(turno);
    }

    @Override
    public Turno update(Turno turno) {
        if (turno.getIdTurno() == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo para actualizar");
        }
        Turno existente = repo.findById(turno.getIdTurno())
                .orElseThrow(() -> new RuntimeException("Turno no encontrado con ID: " + turno.getIdTurno()));
        existente.setHoraInicio(turno.getHoraInicio());
        existente.setHoraFinal(turno.getHoraFinal());
        existente.setDiasSemana(turno.getDiasSemana());
        return repo.save(existente);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new RuntimeException("Turno no encontrado con ID: " + id);
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Turno> findAll() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Turno> findById(Long id) {
        if (id == null || id <= 0) throw new IllegalArgumentException("El ID debe ser positivo");
        return repo.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Turno> findByDia(String dia) {
        if (dia == null || dia.isBlank()) throw new IllegalArgumentException("El día no puede estar vacío");
        return repo.findByDiasSemanaContainingIgnoreCase(dia.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Turno> findByHora(LocalTime hora) {
        if (hora == null) throw new IllegalArgumentException("La hora no puede ser nula");
        return repo.findByHoraInicioLessThanEqualAndHoraFinalGreaterThanEqual(hora, hora);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return repo.count();
    }
}