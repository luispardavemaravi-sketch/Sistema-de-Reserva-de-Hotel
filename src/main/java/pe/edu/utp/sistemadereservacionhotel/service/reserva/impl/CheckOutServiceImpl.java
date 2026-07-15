package pe.edu.utp.sistemadereservacionhotel.service.reserva.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.CheckOut;
import pe.edu.utp.sistemadereservacionhotel.repository.reserva.CheckOutRepository;
import pe.edu.utp.sistemadereservacionhotel.service.reserva.CheckOutService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class CheckOutServiceImpl implements CheckOutService {

    private final CheckOutRepository repo;

    @Override
    public CheckOut save(CheckOut checkOut) {
        if (checkOut.getIdCheckOut() != null) {
            throw new IllegalArgumentException("Para actualizar use el método update");
        }
        if (repo.findByReserva_IdReserva(checkOut.getReserva().getIdReserva()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un CheckOut para la reserva: " + checkOut.getReserva().getIdReserva());
        }
        checkOut.setFechaHoraRealSalida(LocalDateTime.now(ZoneId.of("America/Lima")));
        return repo.save(checkOut);
    }

    @Override
    public CheckOut update(CheckOut checkOut) {
        if (checkOut.getIdCheckOut() == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo para actualizar");
        }
        CheckOut existente = repo.findById(checkOut.getIdCheckOut())
                .orElseThrow(() -> new RuntimeException("CheckOut no encontrado con ID: " + checkOut.getIdCheckOut()));

        existente.setMultaPorRetraso(checkOut.getMultaPorRetraso());

        return repo.save(existente);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("CheckOut no encontrado con ID: " + id);
        }
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CheckOut> findAll() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CheckOut> findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo");
        }
        return repo.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CheckOut> findByReserva(Long idReserva) {
        return repo.findByReserva_IdReserva(idReserva);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CheckOut> findByRangoFecha(LocalDateTime inicio, LocalDateTime fin) {
        if (inicio.isAfter(fin)) {
            throw new IllegalArgumentException("La fecha inicio no puede ser posterior a la fecha fin");
        }
        return repo.findByFechaHoraRealSalidaBetween(inicio, fin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CheckOut> findConMulta(Double montoMinimo) {
        if (montoMinimo < 0) {
            throw new IllegalArgumentException("El monto mínimo no puede ser negativo");
        }
        return repo.findByMultaPorRetrasoGreaterThan(montoMinimo);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return repo.count();
    }
}