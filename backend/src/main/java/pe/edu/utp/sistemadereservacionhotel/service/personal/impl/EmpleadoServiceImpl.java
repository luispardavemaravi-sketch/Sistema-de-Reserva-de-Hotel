package pe.edu.utp.sistemadereservacionhotel.service.personal.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sistemadereservacionhotel.dto.personal.EmpleadoRequestDTO;
import pe.edu.utp.sistemadereservacionhotel.dto.personal.EmpleadoResponseDTO;
import pe.edu.utp.sistemadereservacionhotel.model.personal.Empleado;
import pe.edu.utp.sistemadereservacionhotel.model.personal.Turno;
import pe.edu.utp.sistemadereservacionhotel.model.personal.CargoEmpleado;
import pe.edu.utp.sistemadereservacionhotel.repository.personal.EmpleadoRepository;
import pe.edu.utp.sistemadereservacionhotel.repository.personal.TurnoRepository;
import pe.edu.utp.sistemadereservacionhotel.service.personal.EmpleadoService;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.DuplicadoException;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.RecursoNoEncontradoException;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.ValidacionException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación núcleo de la lógica de recursos humanos.
 * Centraliza validaciones de integridad referencial y normativas de borrado lógico.
 */
@RequiredArgsConstructor
@Service
public class EmpleadoServiceImpl implements EmpleadoService {

    private final EmpleadoRepository empleadoRepo;
    private final TurnoRepository turnoRepo;

    private static final String ENTIDAD_NOMBRE = "Empleado";

    @Override
    @Transactional
    public EmpleadoResponseDTO registrarEmpleado(EmpleadoRequestDTO dto) {
        String emailProcesado = dto.email().trim().toLowerCase();

        if (empleadoRepo.existsByEmail(emailProcesado)) {
            throw new DuplicadoException("El correo electrónico corporativo ya se encuentra asignado.");
        }

        Turno turnoAsignado = turnoRepo.findById(dto.idTurno())
                .orElseThrow(() -> new RecursoNoEncontradoException("Turno", dto.idTurno()));

        Empleado entidad = new Empleado();
        entidad.setEmail(emailProcesado);
        entidad.setEstadoActivo(true);

        // Uso del método de extracción para evitar código duplicado (SonarLint Fix)
        mapearDatosComunes(entidad, dto, turnoAsignado);

        return mapearADto(empleadoRepo.save(entidad));
    }

    @Override
    @Transactional
    public EmpleadoResponseDTO actualizarEmpleado(Long id, EmpleadoRequestDTO dto) {
        Empleado existente = empleadoRepo.findById(id)
                // CORRECCIÓN: Variable sin comillas
                .orElseThrow(() -> new RecursoNoEncontradoException(ENTIDAD_NOMBRE, id));

        String emailProcesado = dto.email().trim().toLowerCase();
        if (!existente.getEmail().equals(emailProcesado) && empleadoRepo.existsByEmail(emailProcesado)) {
            throw new DuplicadoException("El nuevo correo electrónico ya está en uso por otro empleado.");
        }

        Turno turnoAsignado = turnoRepo.findById(dto.idTurno())
                .orElseThrow(() -> new RecursoNoEncontradoException("Turno", dto.idTurno()));

        existente.setEmail(emailProcesado);

        // Uso del método de extracción para evitar código duplicado (SonarLint Fix)
        mapearDatosComunes(existente, dto, turnoAsignado);

        return mapearADto(empleadoRepo.save(existente));
    }

    @Override
    @Transactional
    public void darDeBajaEmpleado(Long id) {
        Empleado existente = empleadoRepo.findById(id)
                // CORRECCIÓN: Variable sin comillas
                .orElseThrow(() -> new RecursoNoEncontradoException(ENTIDAD_NOMBRE, id));

        if (!existente.getEstadoActivo()) {
            throw new ValidacionException("El empleado ya se encuentra inactivo.");
        }

        existente.setEstadoActivo(false);
        empleadoRepo.save(existente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpleadoResponseDTO> listarActivos() {
        return empleadoRepo.findByEstadoActivoTrue().stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EmpleadoResponseDTO buscarPorId(Long id) {
        Empleado empleado = empleadoRepo.findById(id)
                // CORRECCIÓN: Variable sin comillas
                .orElseThrow(() -> new RecursoNoEncontradoException(ENTIDAD_NOMBRE, id));
        return mapearADto(empleado);
    }

    @Override
    @Transactional(readOnly = true)
    public EmpleadoResponseDTO buscarPorEmail(String email) {
        Empleado empleado = empleadoRepo.findByEmailAndEstadoActivoTrue(email.trim().toLowerCase())
                .orElseThrow(() -> new RecursoNoEncontradoException("Empleado no encontrado con el correo provisto."));
        return mapearADto(empleado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpleadoResponseDTO> buscarPorNombre(String nombre) {
        return empleadoRepo.findByNombreContainingIgnoreCaseAndEstadoActivoTrue(nombre.trim()).stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpleadoResponseDTO> buscarPorApellido(String apellido) {
        return empleadoRepo.findByApellidoContainingIgnoreCaseAndEstadoActivoTrue(apellido.trim()).stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpleadoResponseDTO> buscarPorCargo(CargoEmpleado cargo) {
        if (cargo == null) {
            throw new ValidacionException("El cargo es obligatorio para realizar la búsqueda.");
        }
        return empleadoRepo.findByCargoAndEstadoActivoTrue(cargo).stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpleadoResponseDTO> buscarPorCiudad(String ciudad) {
        return empleadoRepo.findByCiudadContainingIgnoreCaseAndEstadoActivoTrue(ciudad.trim()).stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    /**
     * Extrae el código duplicado de asignación de atributos para cumplir con el principio DRY.
     */
    private void mapearDatosComunes(Empleado entidad, EmpleadoRequestDTO dto, Turno turnoAsignado) {
        entidad.setNombre(dto.nombre().trim());
        entidad.setApellido(dto.apellido().trim());
        entidad.setTelefono(dto.telefono().trim());
        entidad.setCargo(dto.cargo());
        entidad.setEspecialidad(dto.especialidad());
        entidad.setDireccion(dto.direccion().trim());
        entidad.setCiudad(dto.ciudad().trim());
        entidad.setTurno(turnoAsignado);
    }

    /**
     * Mapeado de transferencia de datos.
     * Convierte la entidad administrada por JPA en un registro inmutable.
     */
    private EmpleadoResponseDTO mapearADto(Empleado entidad) {
        return new EmpleadoResponseDTO(
                entidad.getIdEmpleado(),
                entidad.getNombre(),
                entidad.getApellido(),
                entidad.getEmail(),
                entidad.getTelefono(),
                entidad.getCargo(),
                entidad.getEspecialidad(),
                entidad.getDireccion(),
                entidad.getCiudad(),
                entidad.getTurno() != null ? entidad.getTurno().getIdTurno() : null,
                entidad.getEstadoActivo()
        );
    }
}