package pe.edu.utp.sistemadereservacionhotel.service.personal.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sistemadereservacionhotel.dto.personal.TurnoDTO;
import pe.edu.utp.sistemadereservacionhotel.model.personal.Turno;
import pe.edu.utp.sistemadereservacionhotel.repository.personal.TurnoRepository;
import pe.edu.utp.sistemadereservacionhotel.service.personal.TurnoService;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.RecursoNoEncontradoException;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.ValidacionException;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación núcleo para la arquitectura de horarios del personal.
 * Centraliza las validaciones se traslape cronológico y el mapeo de transferencia de datos.
 */
@RequiredArgsConstructor
@Service
public class TurnoServiceImpl implements TurnoService {

    private final TurnoRepository repo;

    /**
     * Válida y persiste un nuevo bloque operativo.
     * Implementa protección contra turnos ilógicos (hora de inicio posterior a la final).
     */
    @Override
    @Transactional
    public TurnoDTO registrarTurno(TurnoDTO dto) {
        validarCoherenciaHoraria(dto.horaInicio(), dto.horaFinal());

        Turno entidad = new Turno();
        entidad.setHoraInicio(dto.horaInicio());
        entidad.setHoraFinal(dto.horaFinal());
        entidad.setDiasSemana(dto.diasSemana());

        return mapearADto(repo.save(entidad));
    }

    /**
     * Actualiza la malla horaria validando su integridad previamente.
     */
    @Override
    @Transactional
    public TurnoDTO actualizarTurno(Long id, TurnoDTO dto) {
        Turno existente = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Turno", id));

        validarCoherenciaHoraria(dto.horaInicio(), dto.horaFinal());

        existente.setHoraInicio(dto.horaInicio());
        existente.setHoraFinal(dto.horaFinal());
        existente.setDiasSemana(dto.diasSemana());

        return mapearADto(repo.save(existente));
    }

    @Override
    @Transactional
    public void eliminarTurno(Long id) {
        if (!repo.existsById(id)) {
            throw new RecursoNoEncontradoException("Turno", id);
        }
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TurnoDTO> listarTodos() {
        return repo.findAll().stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TurnoDTO buscarPorId(Long id) {
        Turno turno = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Turno", id));
        return mapearADto(turno);
    }

    /**
     * Delega en Spring Data JPA la intersección contra la tabla relacional de Días (@ElementCollection).
     *
     * @param dia Instancia estricta del Enum DayOfWeek.
     */
    @Override
    @Transactional(readOnly = true)
    public List<TurnoDTO> buscarPorDia(DayOfWeek dia) {
        if (dia == null) {
            throw new ValidacionException("El parámetro de día es obligatorio para la búsqueda.");
        }
        return repo.findByDiasSemana(dia).stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TurnoDTO> buscarTurnosActivosEnHora(LocalTime hora) {
        if (hora == null) {
            throw new ValidacionException("El parámetro de hora es obligatorio.");
        }
        return repo.findByHoraInicioLessThanEqualAndHoraFinalGreaterThanEqual(hora, hora).stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    /**
     * Algoritmo de protección para detectar incoherencias cronológicas en un bloque de horario.
     */
    private void validarCoherenciaHoraria(LocalTime inicio, LocalTime fin) {
        if (inicio.isAfter(fin)) {
            // Nota: Esta validación asume turnos dentro del mismo día calendario.
            // Si el hotel posee turnos nocturnos (ej. 22:00 a 06:00), esta lógica debe ampliarse.
            throw new ValidacionException("La hora de inicio no puede ser posterior a la hora final en el mismo día.");
        }
    }

    /**
     * Factory Method interno para desacoplar el motor de persistencia del envío JSON.
     */
    private TurnoDTO mapearADto(Turno entidad) {
        return new TurnoDTO(
                entidad.getIdTurno(),
                entidad.getHoraInicio(),
                entidad.getHoraFinal(),
                entidad.getDiasSemana()
        );
    }
}