package pe.edu.utp.sistemadereservacionhotel.service.finanzas.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sistemadereservacionhotel.model.finanzas.ComprobantePago;
import pe.edu.utp.sistemadereservacionhotel.repository.finanzas.ComprobantePagoRepository;
import pe.edu.utp.sistemadereservacionhotel.service.finanzas.ComprobantePagoService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class ComprobantePagoServiceImpl implements ComprobantePagoService {

    private final ComprobantePagoRepository repo;

    @Override
    public ComprobantePago save(ComprobantePago comprobante) {
        if (comprobante.getIdComprobante() != null) {
            throw new IllegalArgumentException("Para actualizar use el método update");
        }
        if (repo.existsByNumeroSerie(comprobante.getNumeroSerie())) {
            throw new IllegalArgumentException("Ya existe un comprobante con la serie: " + comprobante.getNumeroSerie());
        }
        comprobante.setFechaEmision(LocalDateTime.now());
        return repo.save(comprobante);
    }

    @Override
    public ComprobantePago update(ComprobantePago comprobante) {
        if (comprobante.getIdComprobante() == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo para actualizar");
        }
        ComprobantePago existente = repo.findById(comprobante.getIdComprobante())
                .orElseThrow(() -> new RuntimeException("Comprobante no encontrado con ID: " + comprobante.getIdComprobante()));
        existente.setMontoTotal(comprobante.getMontoTotal());
        return repo.save(existente);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new RuntimeException("Comprobante no encontrado con ID: " + id);
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComprobantePago> findAll() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ComprobantePago> findById(Long id) {
        if (id == null || id <= 0) throw new IllegalArgumentException("El ID debe ser positivo");
        return repo.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ComprobantePago> findByNumeroSerie(String serie) {
        if (serie == null || serie.isBlank()) throw new IllegalArgumentException("La serie no puede estar vacía");
        return repo.findByNumeroSerie(serie.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ComprobantePago> findByReserva(Long idReserva) {
        return repo.findByReserva_IdReserva(idReserva);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComprobantePago> findByRangoFecha(LocalDateTime inicio, LocalDateTime fin) {
        if (inicio.isAfter(fin)) throw new IllegalArgumentException("Fecha inicio no puede ser posterior a fecha fin");
        return repo.findByFechaEmisionBetween(inicio, fin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComprobantePago> findByRangoMonto(Double min, Double max) {
        if (min > max) throw new IllegalArgumentException("El monto mínimo no puede ser mayor al máximo");
        return repo.findByMontoTotalBetween(min, max);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return repo.count();
    }
}