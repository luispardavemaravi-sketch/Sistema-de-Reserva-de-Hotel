package pe.edu.utp.sistemadereservacionhotel.controller.reserva;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.sistemadereservacionhotel.dto.reserva.ReservaRequestDTO;
import pe.edu.utp.sistemadereservacionhotel.dto.reserva.ReservaResponseDTO;
import pe.edu.utp.sistemadereservacionhotel.service.reserva.ReservaService;

@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService service;

    @PostMapping
    public ResponseEntity<ReservaResponseDTO> crear(@RequestBody ReservaRequestDTO dto) {
        return ResponseEntity.ok(service.crearReserva(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<ReservaResponseDTO> buscarPorCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(service.buscarPorCodigo(codigo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservaResponseDTO> actualizar(@PathVariable Long id, @RequestBody ReservaRequestDTO dto) {
        return ResponseEntity.ok(service.actualizarReserva(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        service.cancelarReserva(id);
        return ResponseEntity.noContent().build();
    }
}