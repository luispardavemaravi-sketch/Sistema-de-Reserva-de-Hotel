package pe.edu.utp.sistemadereservacionhotel.service.reserva.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sistemadereservacionhotel.dto.reserva.AcompananteRequestDTO;
import pe.edu.utp.sistemadereservacionhotel.dto.reserva.AcompananteResponseDTO;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.Acompanante;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.Parentesco;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.Reserva;
import pe.edu.utp.sistemadereservacionhotel.repository.reserva.AcompananteRepository;
import pe.edu.utp.sistemadereservacionhotel.repository.reserva.ReservaRepository;
import pe.edu.utp.sistemadereservacionhotel.service.reserva.AcompananteService;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.RecursoNoEncontradoException;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.ValidacionException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación transaccional del servicio de acompañantes.
 * Gestiona el registro y consulta de ocupantes adicionales vinculados a una reserva.
 */
@RequiredArgsConstructor
@Service
public class AcompananteServiceImpl implements AcompananteService {

    private final AcompananteRepository repo;
    private final ReservaRepository reservaRepo;
    private static final String ENTIDAD = "Acompañante";

    @Override
    @Transactional
    public AcompananteResponseDTO registrarAcompanante(AcompananteRequestDTO dto) {
        Reserva reserva = reservaRepo.findById(dto.idReserva())
                .orElseThrow(() -> new RecursoNoEncontradoException("Reserva", dto.idReserva()));

        Acompanante entidad = new Acompanante();
        mapearDatos(entidad, dto, reserva);

        return mapearADto(repo.save(entidad));
    }

    @Override
    @Transactional
    public AcompananteResponseDTO actualizarAcompanante(Long id, AcompananteRequestDTO dto) {
        Acompanante existente = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(ENTIDAD, id));

        Reserva reserva = reservaRepo.findById(dto.idReserva())
                .orElseThrow(() -> new RecursoNoEncontradoException("Reserva", dto.idReserva()));

        mapearDatos(existente, dto, reserva);

        return mapearADto(repo.save(existente));
    }

    @Override
    @Transactional
    public void eliminarAcompanante(Long id) {
        if (!repo.existsById(id)) {
            throw new RecursoNoEncontradoException(ENTIDAD, id);
        }
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AcompananteResponseDTO> listarTodos() {
        return repo.findAll().stream().map(this::mapearADto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AcompananteResponseDTO buscarPorId(Long id) {
        return mapearADto(repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(ENTIDAD, id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AcompananteResponseDTO> buscarPorReserva(Long idReserva) {
        return repo.findByReserva_IdReserva(idReserva).stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AcompananteResponseDTO> buscarPorDocumentoIdentidad(String documento) {
        return repo.findByDocumentoIdentidad(documento.trim()).stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    /**
     * Extrae la lógica de mapeo para evitar duplicidad de código (Cumplimiento DRY).
     */
    private void mapearDatos(Acompanante e, AcompananteRequestDTO dto, Reserva r) {
        // Ahora como documentoIdentidad es String en el DTO, .trim() funcionará
        e.setNombreCompleto(dto.nombre().trim());
        e.setApellidos(dto.apellidos().trim());
        e.setDocumentoIdentidad(dto.documentoIdentidad().trim());

        try {
            e.setParentesco(Parentesco.valueOf(dto.parentesco().toUpperCase()));
        } catch (IllegalArgumentException ex) {
            throw new ValidacionException("Parentesco inválido: " + dto.parentesco());
        }
        e.setReserva(r);
    }

    private AcompananteResponseDTO mapearADto(Acompanante e) {
        return new AcompananteResponseDTO(
                e.getIdAcompanante(),
                e.getNombreCompleto(),
                e.getApellidos(),
                e.getDocumentoIdentidad(),
                e.getParentesco().name(), // Convertimos Enum a String para el DTO
                e.getReserva().getIdReserva()
        );
    }
}