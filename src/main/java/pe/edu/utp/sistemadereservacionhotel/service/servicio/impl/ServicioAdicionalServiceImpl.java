package pe.edu.utp.sistemadereservacionhotel.service.servicio.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sistemadereservacionhotel.dto.servicio.ServicioAdicionalDTO;
import pe.edu.utp.sistemadereservacionhotel.model.servicio.ServicioAdicional;
import pe.edu.utp.sistemadereservacionhotel.repository.servicio.ServicioAdicionalRepository;
import pe.edu.utp.sistemadereservacionhotel.service.servicio.ServicioAdicionalService;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.DuplicadoException;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.RecursoNoEncontradoException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación de la gestión operativa de servicios.
 * Se enfoca exclusivamente en la administración del inventario de servicios,
 * delegando la valoración financiera a CatalogoServicio.
 */
@RequiredArgsConstructor
@Service
public class ServicioAdicionalServiceImpl implements ServicioAdicionalService {

    private final ServicioAdicionalRepository repo;
    private static final String ENTIDAD = "Servicio Adicional";

    @Override
    @Transactional
    public ServicioAdicionalDTO registrarServicio(ServicioAdicionalDTO dto) {
        if (repo.existsByNombre(dto.nombre().trim())) {
            throw new DuplicadoException("Ya existe un servicio llamado: " + dto.nombre());
        }

        ServicioAdicional entidad = new ServicioAdicional();
        entidad.setNombre(dto.nombre().trim());
        entidad.setDescripcion(dto.descripcion());
        entidad.setEstaDisponible(dto.estaDisponible());

        return mapearADto(repo.save(entidad));
    }

    @Override
    @Transactional
    public ServicioAdicionalDTO actualizarServicio(Long id, ServicioAdicionalDTO dto) {
        ServicioAdicional existente = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(ENTIDAD, id));

        existente.setNombre(dto.nombre().trim());
        existente.setDescripcion(dto.descripcion());
        existente.setEstaDisponible(dto.estaDisponible());

        return mapearADto(repo.save(existente));
    }

    @Override
    @Transactional
    public void eliminarServicio(Long id) {
        if (!repo.existsById(id)) throw new RecursoNoEncontradoException(ENTIDAD, id);
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServicioAdicionalDTO> listarTodos() {
        return repo.findAll().stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ServicioAdicionalDTO buscarPorId(Long id) {
        return mapearADto(repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(ENTIDAD, id)));
    }

    @Override
    @Transactional(readOnly = true)
    public ServicioAdicionalDTO buscarPorNombre(String nombre) {
        return mapearADto(repo.findByNombre(nombre.trim())
                .orElseThrow(() -> new RecursoNoEncontradoException(ENTIDAD, "Nombre: " + nombre)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServicioAdicionalDTO> buscarPorNombreContiene(String nombre) {
        return repo.findByNombreContainingIgnoreCase(nombre.trim()).stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServicioAdicionalDTO> buscarPorDisponibilidad(Boolean disponible) {
        return repo.findByEstaDisponible(disponible).stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorNombre(String nombre) {
        return repo.existsByNombre(nombre.trim());
    }

    private ServicioAdicionalDTO mapearADto(ServicioAdicional e) {
        return new ServicioAdicionalDTO(e.getIdServicio(), e.getNombre(), e.getDescripcion(), e.getEstaDisponible());
    }
}