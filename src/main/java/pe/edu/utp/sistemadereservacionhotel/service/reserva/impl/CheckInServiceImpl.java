package pe.edu.utp.sistemadereservacionhotel.service.reserva.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.CheckIn;
import pe.edu.utp.sistemadereservacionhotel.repository.reserva.CheckInRepository;
import pe.edu.utp.sistemadereservacionhotel.service.reserva.CheckInService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class CheckInServiceImpl implements CheckInService {

    private final CheckInRepository repo;

    @Override
    public CheckIn save(CheckIn checkIn) {
        if (checkIn.getIdCheckIn() != null) {
            throw new IllegalArgumentException("Para actualizar use el método update");
        }
        if (repo.findByReserva_IdReserva(checkIn.getReserva().getIdReserva()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un CheckIn para la reserva: " + checkIn.getReserva().getIdReserva());
        }
        checkIn.setFechaHoraRealEntrada(LocalDateTime.now());
        return repo.save(checkIn);
    }

    @Override
    public CheckIn update(CheckIn checkIn) {
        if (checkIn.getIdCheckIn() == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo para actualizar");
        }
        CheckIn existente = repo.findById(checkIn.getIdCheckIn())
                .orElseThrow(() -> new RuntimeException("CheckIn no encontrado con ID: " + checkIn.getIdCheckIn()));

        existente.setObservaciones(checkIn.getObservaciones());

        return repo.save(existente);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("CheckIn no encontrado con ID: " + id);
        }
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CheckIn> findAll() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CheckIn> findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo");
        }
        return repo.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CheckIn> findByReserva(Long idReserva) {
        return repo.findByReserva_IdReserva(idReserva);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CheckIn> findByRangoFecha(LocalDateTime inicio, LocalDateTime fin) {
        if (inicio.isAfter(fin)) {
            throw new IllegalArgumentException("La fecha inicio no puede ser posterior a la fecha fin");
        }
        return repo.findByFechaHoraRealEntradaBetween(inicio, fin);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return repo.count();
    }
}