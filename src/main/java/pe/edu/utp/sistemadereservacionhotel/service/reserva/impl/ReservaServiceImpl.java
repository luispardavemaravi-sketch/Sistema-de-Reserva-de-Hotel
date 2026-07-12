package pe.edu.utp.sistemadereservacionhotel.service.reserva.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.Reserva;
import pe.edu.utp.sistemadereservacionhotel.repository.reserva.ReservaRepository;
import pe.edu.utp.sistemadereservacionhotel.service.reserva.ReservaServicio;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class ReservaServiceImpl implements ReservaServicio {
    private final ReservaRepository repo;

    //Para guardar reservas
    @Override
    public Reserva save(Reserva reserva) {
        if (reserva.getIdReserva() != null) {
            throw new IllegalArgumentException("Para actualizar use el metodo update");
        }
        if (repo.existsByCodigoReserva(reserva.getCodigoReserva())) {
            throw new IllegalArgumentException("La reserva ya existe");
        }
        reserva.setFechaReserva(LocalDateTime.now());
        return repo.save(reserva);
    }


    //Actualizar Reserva
    @Override
    public Reserva update(Reserva reserva) {
        if (reserva.getCodigoReserva() == null) {
            throw new IllegalArgumentException("El ID  no puede estar vacio para actualizar.");
        }

        //1. Buscar reserva existente en DB
        Reserva existente = repo.findByCodigoReserva(reserva.getCodigoReserva())
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con codigo: " + reserva.getCodigoReserva()));

        //2. Actualizar reserva solo con los campos que llegaron

        existente.setIdReserva(reserva.getIdReserva());
        existente.setCodigoReserva(reserva.getCodigoReserva());
        existente.setFechaReserva(reserva.getFechaReserva());
        existente.setFechaEntradaPlanificada(reserva.getFechaEntradaPlanificada());
        existente.setMontoTotalEstimado(reserva.getMontoTotalEstimado());
        existente.setHuesped(reserva.getHuesped());
        existente.setEstadoReserva(reserva.getEstadoReserva());
        existente.setCheckIn(reserva.getCheckIn());
        existente.setCheckOut(reserva.getCheckOut());
        existente.setAcompanantes(reserva.getAcompanantes());
        return repo.save(existente);
    }

    //Eliminar reserva
    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Reserva no encontrada con codigo: " + id);
        }
        repo.deleteById(id);
    }

    //Lista todas las reservas
    @Override
    @Transactional(readOnly = true)
    public List<Reserva> findAll() {
        return repo.findAll();
    }

    //Buscar por Id
    @Override
    @Transactional(readOnly = true)
    public Optional<Reserva> findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser positivo");
        }
        return repo.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Reserva> findByCodigoReserva(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código no puede estar vacío");
        }
        return repo.findByCodigoReserva(codigo.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reserva> findByHuesped(Long idHuesped) {
        if (idHuesped == null || idHuesped <= 0) {
            throw new IllegalArgumentException("El ID del huésped debe ser positivo");
        }
        return repo.findByHuesped_IdHuesped(idHuesped);
    }


    @Override
    @Transactional(readOnly = true)
    public List<Reserva> findByEstado(Long idEstado) {
        return repo.findByEstadoReserva_IdEstado(idEstado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reserva> findByFechaEntrada(LocalDate fecha) {
        return repo.findByFechaEntradaPlanificada(fecha);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reserva> findByRangoFechas(LocalDate inicio, LocalDate fin) {
        if (inicio.isAfter(fin)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha fin");
        }
        return repo.findByFechaEntradaPlanificadaBetween(inicio, fin);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return repo.count();
    }
}
