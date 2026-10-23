package pe.edu.utp.sistemadereservacionhotel.controller.reserva;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.sistemadereservacionhotel.dto.reserva.AcompananteRequestDTO;
import pe.edu.utp.sistemadereservacionhotel.dto.reserva.AcompananteResponseDTO;
import pe.edu.utp.sistemadereservacionhotel.service.reserva.AcompananteService;

import java.util.List;

@RestController
@RequestMapping("/api/reservas/acompanantes")
@RequiredArgsConstructor
public class AcompananteController {

    private final AcompananteService service;

    @PostMapping
    public ResponseEntity<AcompananteResponseDTO> registrar(@RequestBody AcompananteRequestDTO dto) {
        return ResponseEntity.ok(service.registrarAcompanante(dto));
    }

    @GetMapping("/reserva/{idReserva}")
    public ResponseEntity<List<AcompananteResponseDTO>> buscarPorReserva(@PathVariable Long idReserva) {
        return ResponseEntity.ok(service.buscarPorReserva(idReserva));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AcompananteResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AcompananteResponseDTO> actualizar(@PathVariable Long id, @RequestBody AcompananteRequestDTO dto) {
        return ResponseEntity.ok(service.actualizarAcompanante(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminarAcompanante(id);
        return ResponseEntity.noContent().build();
    }
}