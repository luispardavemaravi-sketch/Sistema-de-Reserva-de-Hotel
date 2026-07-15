package pe.edu.utp.sistemadereservacionhotel.service.habitacion.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sistemadereservacionhotel.dto.habitacion.PisoDTO;
import pe.edu.utp.sistemadereservacionhotel.model.habitacion.Piso;
import pe.edu.utp.sistemadereservacionhotel.repository.habitacion.PisoRepository;
import pe.edu.utp.sistemadereservacionhotel.service.habitacion.PisoService;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.DuplicadoException;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.RecursoNoEncontradoException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio transaccional encargado de administrar la estructura arquitectónica del hotel.
 * Gestiona los niveles (pisos) y alas (sectores) garantizando la no duplicidad estructural.
 */
@RequiredArgsConstructor
@Service
public class PisoServiceImpl implements PisoService {

    private final PisoRepository repo;

    /**
     * Da de alta un nuevo nivel en el hotel.
     * Implementa un mecanismo Fail-Fast para abortar si el número de piso ya existe.
     */
    @Override
    @Transactional
    public PisoDTO registrarPiso(PisoDTO dto) {
        if (repo.existsByNumeroPiso(dto.numeroPiso())) {
            throw new DuplicadoException("Ya existe el piso número: " + dto.numeroPiso());
        }

        Piso entidad = new Piso();
        entidad.setNumeroPiso(dto.numeroPiso());
        entidad.setSector(dto.sector());

        return mapearADto(repo.save(entidad));
    }

    /**
     * Modifica los datos de un piso existente.
     * Valida la unicidad para evitar que se actualice un piso usurpando el número de otro ya registrado.
     */
    @Override
    @Transactional
    public PisoDTO actualizarPiso(Long id, PisoDTO dto) {
        Piso existente = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Piso", id));

        if (!existente.getNumeroPiso().equals(dto.numeroPiso()) &&
                repo.existsByNumeroPiso(dto.numeroPiso())) {
            throw new DuplicadoException("Ya existe otro registro con el piso número: " + dto.numeroPiso());
        }

        existente.setNumeroPiso(dto.numeroPiso());
        existente.setSector(dto.sector());

        return mapearADto(repo.save(existente));
    }

    @Override
    @Transactional
    public void eliminarPiso(Long id) {
        if (!repo.existsById(id)) {
            throw new RecursoNoEncontradoException("Piso", id);
        }
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PisoDTO> listarTodos() {
        return repo.findAll().stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PisoDTO buscarPorId(Long id) {
        return mapearADto(repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Piso", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public PisoDTO buscarPorNumero(Integer numero) {
        return mapearADto(repo.findByNumeroPiso(numero)
                .orElseThrow(() -> new RecursoNoEncontradoException("Piso no encontrado: " + numero)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PisoDTO> buscarPorSector(String sector) {
        return repo.findBySectorContainingIgnoreCase(sector.trim()).stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    /**
     * Factory Method interno para convertir la entidad de dominio a DTO de transferencia,
     * garantizando que el controlador nunca tenga contacto directo con la base de datos.
     */
    private PisoDTO mapearADto(Piso entidad) {
        return new PisoDTO(
                entidad.getIdPiso(),
                entidad.getNumeroPiso(),
                entidad.getSector()
        );
    }
}