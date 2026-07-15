package pe.edu.utp.sistemadereservacionhotel.service.servicio.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sistemadereservacionhotel.dto.AreaHotelDTO;
import pe.edu.utp.sistemadereservacionhotel.model.servicio.AreaHotel;
import pe.edu.utp.sistemadereservacionhotel.repository.servicio.AreaHotelRepository;
import pe.edu.utp.sistemadereservacionhotel.service.servicio.AreaHotelService;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AreaHotelServiceImpl implements AreaHotelService {

    private final AreaHotelRepository repo;
    private static final String ENTIDAD = "Área del Hotel";

    @Override
    @Transactional
    public AreaHotelDTO registrarArea(AreaHotelDTO dto) {
        if (repo.existsByNombreArea(dto.nombreArea())) {
            throw new DuplicadoException("Ya existe un área con el nombre: " + dto.nombreArea());
        }
        AreaHotel entidad = new AreaHotel();
        entidad.setNombreArea(dto.nombreArea().trim());
        entidad.setUbicacion(dto.ubicacion().trim());
        return mapearADto(repo.save(entidad));
    }

    @Override
    @Transactional
    public AreaHotelDTO actualizarArea(Long id, AreaHotelDTO dto) {
        AreaHotel existente = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(ENTIDAD, id));

        existente.setNombreArea(dto.nombreArea().trim());
        existente.setUbicacion(dto.ubicacion().trim());
        return mapearADto(repo.save(existente));
    }

    @Override
    @Transactional
    public void eliminarArea(Long id) {
        if (!repo.existsById(id)) throw new RecursoNoEncontradoException(ENTIDAD, id);
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AreaHotelDTO> listarTodas() {
        return repo.findAll().stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AreaHotelDTO buscarPorId(Long id) {
        return mapearADto(repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(ENTIDAD, id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AreaHotelDTO> buscarPorUbicacion(String ubicacion) {
        if (ubicacion == null || ubicacion.isBlank()) {
            throw new ValidacionException("La ubicación para búsqueda no puede estar vacía.");
        }
        return repo.findByUbicacionContainingIgnoreCase(ubicacion.trim()).stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    private AreaHotelDTO mapearADto(AreaHotel e) {
        return new AreaHotelDTO(e.getIdAreaHotel(), e.getNombreArea(), e.getUbicacion());
    }
}