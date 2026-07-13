package pe.edu.utp.sistemadereservacionhotel.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sistemadereservacionhotel.model.habitacion.PrecioHabitacion;
import pe.edu.utp.sistemadereservacionhotel.repository.habitacion.PrecioHabitacionRepository;
import pe.edu.utp.sistemadereservacionhotel.service.habitacion.PrecioHabitacionService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class PrecioHabitacionServiceImpl implements PrecioHabitacionService {

    private final PrecioHabitacionRepository repo;

    @Override
    public PrecioHabitacion save(PrecioHabitacion precio) {
        if (precio.getIdPrecio() != null) {
            throw new IllegalArgumentException("Para actualizar use el método update");
        }
        if (precio.getFechaInicio().isAfter(precio.getFechaFin())) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha fin");
        }
        return repo.save(precio);
    }

    @Override
    public PrecioHabitacion update(PrecioHabitacion precio) {
        if (precio.getIdPrecio() == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo para actualizar");
        }
        PrecioHabitacion existente = repo.findById(precio.getIdPrecio())
                .orElseThrow(() -> new RuntimeException("Precio no encontrado con ID: " + precio.getIdPrecio()));

        existente.setMonto(precio.getMonto());
        existente.setFechaInicio(precio.getFechaInicio());
        existente.setFechaFin(precio.getFechaFin());

        return repo.save(existente);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Precio no encontrado con ID: " + id);
        }
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrecioHabitacion> findAll() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PrecioHabitacion> findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo");
        }
        return repo.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrecioHabitacion> findByHabitacion(Long idHabitacion) {
        return repo.findByHabitacion_IdHabitacion(idHabitacion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrecioHabitacion> findByRangoFecha(LocalDate inicio, LocalDate fin) {
        if (inicio.isAfter(fin)) {
            throw new IllegalArgumentException("La fecha inicio no puede ser posterior a la fecha fin");
        }
        return repo.findByFechaInicioBetween(inicio, fin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrecioHabitacion> findByRangoMonto(Double montoMin, Double montoMax) {
        if (montoMin > montoMax) {
            throw new IllegalArgumentException("El monto mínimo no puede ser mayor al monto máximo");
        }
        return repo.findByMontoBetween(montoMin, montoMax);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return repo.count();
    }
}