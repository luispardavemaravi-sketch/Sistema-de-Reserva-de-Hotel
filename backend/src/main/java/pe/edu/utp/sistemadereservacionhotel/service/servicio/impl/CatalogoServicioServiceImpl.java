package pe.edu.utp.sistemadereservacionhotel.service.servicio.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sistemadereservacionhotel.dto.servicio.CatalogoServicioDTO;
import pe.edu.utp.sistemadereservacionhotel.model.servicio.CatalogoServicio;
import pe.edu.utp.sistemadereservacionhotel.model.servicio.CategoriaServicio;
import pe.edu.utp.sistemadereservacionhotel.repository.servicio.CatalogoServicioRepository;
import pe.edu.utp.sistemadereservacionhotel.service.servicio.CatalogoServicioService;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación de los servicios del catálogo.
 * Gestiona la transformación entre DTO y Entidades, asegurando la integridad
 * mediante el uso de Enums y excepciones de dominio.
 */
@RequiredArgsConstructor
@Service
public class CatalogoServicioServiceImpl implements CatalogoServicioService {

    private final CatalogoServicioRepository repo;
    private static final String ENTIDAD = "Catálogo de Servicios";

    @Override
    @Transactional
    public CatalogoServicioDTO registrarEnCatalogo(CatalogoServicioDTO dto) {
        CatalogoServicio entidad = new CatalogoServicio();
        entidad.setPrecioVigente(dto.precio());
        // Conversión segura de String a Enum
        entidad.setCategoria(CategoriaServicio.valueOf(dto.categoria().toUpperCase()));
        return mapearADto(repo.save(entidad));
    }

    @Override
    @Transactional
    public CatalogoServicioDTO actualizarEnCatalogo(Long id, CatalogoServicioDTO dto) {
        CatalogoServicio existente = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(ENTIDAD, id));

        existente.setPrecioVigente(dto.precio());
        // Conversión segura de String a Enum
        existente.setCategoria(CategoriaServicio.valueOf(dto.categoria().toUpperCase()));
        return mapearADto(repo.save(existente));
    }

    @Override
    @Transactional
    public void eliminarDelCatalogo(Long id) {
        if (!repo.existsById(id)) throw new RecursoNoEncontradoException(ENTIDAD, id);
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CatalogoServicioDTO> listarTodos() {
        return repo.findAll().stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CatalogoServicioDTO buscarPorId(Long id) {
        return mapearADto(repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(ENTIDAD, id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CatalogoServicioDTO> buscarPorCategoria(String categoria) {
        // Conversión de String a Enum para filtrar en base de datos
        CategoriaServicio enumCategoria = CategoriaServicio.valueOf(categoria.toUpperCase());
        return repo.findByCategoria(enumCategoria).stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CatalogoServicioDTO> buscarPorPrecioMaximo(BigDecimal precioMaximo) {
        if (precioMaximo == null || precioMaximo.signum() < 0) {
            throw new ValidacionException("El precio máximo no puede ser negativo.");
        }
        return repo.findByPrecioVigenteLessThanEqual(precioMaximo).stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    /**
     * Mapeador interno para transformar la entidad a DTO.
     * Convierte el Enum de la entidad a String para el DTO.
     */
    private CatalogoServicioDTO mapearADto(CatalogoServicio e) {
        return new CatalogoServicioDTO(e.getIdCatalogo(), e.getCategoria().name(), e.getPrecioVigente());
    }
}