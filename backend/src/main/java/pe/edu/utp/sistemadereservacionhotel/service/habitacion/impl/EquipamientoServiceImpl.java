package pe.edu.utp.sistemadereservacionhotel.service.habitacion.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sistemadereservacionhotel.dto.habitacion.EquipamientoDTO;
import pe.edu.utp.sistemadereservacionhotel.model.habitacion.Equipamiento;
import pe.edu.utp.sistemadereservacionhotel.repository.habitacion.EquipamientoRepository;
import pe.edu.utp.sistemadereservacionhotel.service.habitacion.EquipamientoService;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.DuplicadoException;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.RecursoNoEncontradoException;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EquipamientoServiceImpl implements EquipamientoService {

    private final EquipamientoRepository repo;

    @Override
    @Transactional
    public EquipamientoDTO registrarEquipamiento(EquipamientoDTO dto) {
        if (repo.existsByNombre(dto.nombre())) {
            throw new DuplicadoException("Ya existe un equipamiento con el nombre: " + dto.nombre());
        }

        Equipamiento entidad = new Equipamiento();
        entidad.setNombre(dto.nombre());
        entidad.setCostoAdicional(dto.costoAdicional()); // Ya recibe BigDecimal

        return mapearADto(repo.save(entidad));
    }

    @Override
    @Transactional
    public void eliminarEquipamiento(Long id) {
        if (!repo.existsById(id)) {
            throw new RecursoNoEncontradoException("Equipamiento", id);
        }
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EquipamientoDTO> listarTodos() {
        return repo.findAll().stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EquipamientoDTO> buscarPorCostoMaximo(BigDecimal costoMaximo) {
        if (costoMaximo == null || costoMaximo.signum() < 0) {
            throw new IllegalArgumentException("El costo máximo no puede ser negativo o nulo");
        }
        return repo.findByCostoAdicionalLessThanEqual(costoMaximo).stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    // Mapeo manual para no acoplar la base de datos a los controladores
    private EquipamientoDTO mapearADto(Equipamiento entidad) {
        return new EquipamientoDTO(
                entidad.getIdEquipamiento(),
                entidad.getNombre(),
                entidad.getCostoAdicional()
        );
    }
}