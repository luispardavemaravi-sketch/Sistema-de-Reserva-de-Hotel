package pe.edu.utp.sistemadereservacionhotel.service.finanzas.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sistemadereservacionhotel.dto.finanzas.CajaAperturaDTO;
import pe.edu.utp.sistemadereservacionhotel.dto.finanzas.CajaResponseDTO;
import pe.edu.utp.sistemadereservacionhotel.model.finanzas.Caja;
import pe.edu.utp.sistemadereservacionhotel.model.personal.Empleado;
import pe.edu.utp.sistemadereservacionhotel.repository.finanzas.CajaRepository;
import pe.edu.utp.sistemadereservacionhotel.repository.personal.EmpleadoRepository;
import pe.edu.utp.sistemadereservacionhotel.service.finanzas.CajaService;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.DuplicadoException;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.RecursoNoEncontradoException;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.ValidacionException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación transaccional para la gestión del ciclo de vida de las cajas.
 * Aplica reglas de negocio estrictas para evitar colisiones operativas, asegurando
 * que un empleado posea un único punto de facturación activo.
 */
@RequiredArgsConstructor
@Service
public class CajaServiceImpl implements CajaService {

    private final CajaRepository cajaRepo;
    private final EmpleadoRepository empleadoRepo;

    /**
     * Inicia una nueva sesión de arqueo.
     * Patrón aplicado: Fail-Fast (Validación de precondiciones antes de la persistencia).
     */
    @Override
    @Transactional
    public CajaResponseDTO abrirCaja(CajaAperturaDTO dto) {
        Empleado empleado = empleadoRepo.findById(dto.idEmpleado())
                .orElseThrow(() -> new RecursoNoEncontradoException("Empleado", dto.idEmpleado()));

        boolean tieneCajaAbierta = cajaRepo.findByEmpleado_IdEmpleado(dto.idEmpleado())
                .stream()
                .anyMatch(Caja::getEstaAbierta);

        if (tieneCajaAbierta) {
            throw new DuplicadoException("El empleado ya posee una sesión de caja abierta en este momento.");
        }

        Caja nuevaCaja = new Caja();
        nuevaCaja.setFecha(LocalDate.now(ZoneId.of("America/Lima")));
        nuevaCaja.setMontoApertura(dto.montoApertura());
        nuevaCaja.setEstaAbierta(true);
        nuevaCaja.setEmpleado(empleado);

        Caja cajaGuardada = cajaRepo.save(nuevaCaja);

        return mapearADto(cajaGuardada);
    }

    /**
     * Clausura una sesión operativa, bloqueando nuevas transacciones en dicho terminal.
     */
    @Override
    @Transactional
    public CajaResponseDTO cerrarCaja(Long idCaja, BigDecimal montoCierre) {
        if (montoCierre == null || montoCierre.signum() < 0) {
            throw new ValidacionException("El monto de cierre no puede ser nulo ni negativo.");
        }

        Caja caja = cajaRepo.findById(idCaja)
                .orElseThrow(() -> new RecursoNoEncontradoException("Caja", idCaja));

        if (!caja.getEstaAbierta()) {
            throw new ValidacionException("La caja seleccionada ya se encuentra cerrada.");
        }

        caja.setMontoCierre(montoCierre);
        caja.setEstaAbierta(false);

        Caja cajaCerrada = cajaRepo.save(caja);
        return mapearADto(cajaCerrada);
    }

    @Override
    @Transactional
    public void eliminarCaja(Long id) {
        if (!cajaRepo.existsById(id)) {
            throw new RecursoNoEncontradoException("Caja", id);
        }
        cajaRepo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public CajaResponseDTO buscarPorId(Long id) {
        Caja caja = cajaRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Caja", id));
        return mapearADto(caja);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CajaResponseDTO> listarTodas() {
        return cajaRepo.findAll().stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CajaResponseDTO> buscarPorEmpleado(Long idEmpleado) {
        return cajaRepo.findByEmpleado_IdEmpleado(idEmpleado).stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CajaResponseDTO> buscarCajasAbiertas() {
        return cajaRepo.findByEstaAbiertaTrue().stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CajaResponseDTO> buscarPorRangoFecha(LocalDate inicio, LocalDate fin) {
        if (inicio.isAfter(fin)) {
            throw new ValidacionException("La fecha de inicio no puede ser posterior a la fecha final.");
        }
        return cajaRepo.findByFechaBetween(inicio, fin).stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    /**
     * Método auxiliar (Factory Method interno) para encapsular la conversión manual Entidad -> DTO.
     */
    private CajaResponseDTO mapearADto(Caja entidad) {
        return new CajaResponseDTO(
                entidad.getIdCaja(),
                entidad.getFecha(),
                entidad.getMontoApertura(),
                entidad.getMontoCierre(),
                entidad.getEstaAbierta(),
                entidad.getEmpleado().getIdEmpleado()
        );
    }
}