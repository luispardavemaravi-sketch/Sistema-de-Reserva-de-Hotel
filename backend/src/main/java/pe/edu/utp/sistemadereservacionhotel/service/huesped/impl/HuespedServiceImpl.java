package pe.edu.utp.sistemadereservacionhotel.service.huesped.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sistemadereservacionhotel.dto.huesped.HuespedRequestDTO;
import pe.edu.utp.sistemadereservacionhotel.dto.huesped.HuespedResponseDTO;
import pe.edu.utp.sistemadereservacionhotel.model.huesped.Huesped;
import pe.edu.utp.sistemadereservacionhotel.repository.huesped.HuespedRepository;
import pe.edu.utp.sistemadereservacionhotel.service.huesped.HuespedService;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.DuplicadoException;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.RecursoNoEncontradoException;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.ValidacionException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación transaccional núcleo para el dominio de Huéspedes.
 * Aplica el patrón Fail-Fast para el control exhaustivo de excepciones de negocio.
 */
@Service
@RequiredArgsConstructor
public class HuespedServiceImpl implements HuespedService {

    private final HuespedRepository repo;
    private static final String ENTIDAD = "Huesped";

    @Override
    @Transactional
    public HuespedResponseDTO registrarHuesped(HuespedRequestDTO dto) {
        String emailProcesado = dto.email().trim().toLowerCase();

        if (repo.existsByEmail(emailProcesado)) {
            throw new DuplicadoException("Ya existe un huésped registrado con el correo: " + dto.email());
        }
        if (repo.existsByDocumentoIdentidad(dto.documentoIdentidad().trim())) {
            throw new DuplicadoException("Ya existe un huésped registrado con el documento: " + dto.documentoIdentidad());
        }

        Huesped entidad = new Huesped();
        entidad.setNombre(dto.nombre().trim());
        entidad.setApellidos(dto.apellidos().trim());
        entidad.setDocumentoIdentidad(dto.documentoIdentidad().trim());
        entidad.setEmail(emailProcesado);
        entidad.setTelefono(dto.telefono() != null ? dto.telefono().trim() : null);
        entidad.setEstadoActivo(true); // Forzado lógico inicial seguro

        return mapearADto(repo.save(entidad));
    }

    @Override
    @Transactional
    public HuespedResponseDTO actualizarHuesped(Long id, HuespedRequestDTO dto) {
        Huesped existente = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(ENTIDAD, id));

        // Bloqueo de seguridad: Evitar alteración de credenciales únicas si chocan con otro usuario
        String emailProcesado = dto.email().trim().toLowerCase();
        if (!existente.getEmail().equals(emailProcesado) && repo.existsByEmail(emailProcesado)) {
            throw new DuplicadoException("El nuevo correo electrónico ya está siendo usado por otro perfil.");
        }
        if (!existente.getDocumentoIdentidad().equals(dto.documentoIdentidad().trim()) &&
                repo.existsByDocumentoIdentidad(dto.documentoIdentidad().trim())) {
            throw new DuplicadoException("El nuevo documento de identidad ya está registrado.");
        }

        existente.setNombre(dto.nombre().trim());
        existente.setApellidos(dto.apellidos().trim());
        existente.setEmail(emailProcesado);
        existente.setDocumentoIdentidad(dto.documentoIdentidad().trim());
        existente.setTelefono(dto.telefono() != null ? dto.telefono().trim() : null);

        return mapearADto(repo.save(existente));
    }

    @Override
    @Transactional
    public void darDeBajaHuesped(Long id) {
        Huesped existente = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(ENTIDAD, id));

        if (!existente.getEstadoActivo()) {
            throw new ValidacionException("El perfil del huésped seleccionado ya se encuentra inactivo.");
        }

        // Borrado lógico mandatorio: Preserva la integridad referencial contable y legal
        existente.setEstadoActivo(false);
        repo.save(existente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HuespedResponseDTO> listarTodos() {
        // Retorna todos los huéspedes activos utilizando el método corregido del repositorio
        return repo.findByEstadoActivoTrue().stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public HuespedResponseDTO buscarPorId(Long id) {
        if (id == null || id <= 0) {
            throw new ValidacionException("El identificador proporcionado debe ser un número positivo.");
        }
        Huesped huesped = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(ENTIDAD, id));

        return mapearADto(huesped);
    }

    @Override
    @Transactional(readOnly = true)
    public HuespedResponseDTO buscarPorEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new ValidacionException("El parámetro de búsqueda por correo no puede ser nulo o vacío.");
        }
        Huesped huesped = repo.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new RecursoNoEncontradoException("Huesped con el correo " + email + " no encontrado."));
        return mapearADto(huesped);
    }

    @Override
    @Transactional(readOnly = true)
    public HuespedResponseDTO buscarPorDocumentoIdentidad(String documento) {
        if (documento == null || documento.isBlank()) {
            throw new ValidacionException("El parámetro de búsqueda por documento no puede ser nulo o vacío.");
        }
        Huesped huesped = repo.findByDocumentoIdentidad(documento.trim())
                .orElseThrow(() -> new RecursoNoEncontradoException("Huesped con documento " + documento + " no encontrado."));
        return mapearADto(huesped);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HuespedResponseDTO> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new ValidacionException("El término de búsqueda por nombre no puede estar vacío.");
        }
        return repo.findByNombreContainingIgnoreCase(nombre.trim()).stream()
                .filter(Huesped::getEstadoActivo) // Asegura no exponer registros dados de baja
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<HuespedResponseDTO> buscarPorApellidos(String apellidos) {
        if (apellidos == null || apellidos.isBlank()) {
            throw new ValidacionException("El término de búsqueda por apellidos no puede estar vacío.");
        }
        return repo.findByApellidosContainingIgnoreCase(apellidos.trim()).stream()
                .filter(Huesped::getEstadoActivo)
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    /**
     * Mapeador manual estandarizado.
     * Centraliza la transformación aislando por completo la entidad del cliente API Rest.
     */
    private HuespedResponseDTO mapearADto(Huesped entidad) {
        return new HuespedResponseDTO(
                entidad.getIdHuesped(),
                entidad.getNombre(),
                entidad.getApellidos(),
                entidad.getDocumentoIdentidad(),
                entidad.getEmail(),
                entidad.getTelefono(),
                entidad.getEstadoActivo()
        );
    }
}