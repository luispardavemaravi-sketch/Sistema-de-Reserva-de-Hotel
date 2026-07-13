package pe.edu.utp.sistemadereservacionhotel.service.servicio.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sistemadereservacionhotel.model.servicio.ConsumoServicio;
import pe.edu.utp.sistemadereservacionhotel.repository.servicio.ConsumoServicioRepository;
import pe.edu.utp.sistemadereservacionhotel.service.servicio.ConsumoServicioService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class ConsumoServicioServiceImpl implements ConsumoServicioService {

    private final ConsumoServicioRepository repo;

    @Override
    public ConsumoServicio save(ConsumoServicio consumo) {
        if (consumo.getIdConsumo() != null) {
            throw new IllegalArgumentException("Para actualizar use el método update");
        }
        consumo.setFechaHora(LocalDateTime.now());
        consumo.setSubTotal(consumo.getCantidad() * consumo.getServicioAdicional().getPrecioUnitario());
        return repo.save(consumo);
    }

    @Override
    public ConsumoServicio update(ConsumoServicio consumo) {
        if (consumo.getIdConsumo() == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo para actualizar");
        }
        ConsumoServicio existente = repo.findById(consumo.getIdConsumo())
                .orElseThrow(() -> new RuntimeException("Consumo no encontrado con ID: " + consumo.getIdConsumo()));
        existente.setCantidad(consumo.getCantidad());
        existente.setSubTotal(consumo.getCantidad() * consumo.getServicioAdicional().getPrecioUnitario());
        return repo.save(existente);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new RuntimeException("Consumo no encontrado con ID: " + id);
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConsumoServicio> findAll() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ConsumoServicio> findById(Long id) {
        if (id == null || id <= 0) throw new IllegalArgumentException("El ID debe ser positivo");
        return repo.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConsumoServicio> findByReserva(Long idReserva) {
        return repo.findByReserva_IdReserva(idReserva);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConsumoServicio> findByServicio(Long idServicio) {
        return repo.findByServicioAdicional_IdServicio(idServicio);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConsumoServicio> findByRangoFecha(LocalDateTime inicio, LocalDateTime fin) {
        if (inicio.isAfter(fin))
            throw new IllegalArgumentException("La fecha inicio no puede ser posterior a la fecha fin");
        return repo.findByFechaHoraBetween(inicio, fin);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return repo.count();
    }
}