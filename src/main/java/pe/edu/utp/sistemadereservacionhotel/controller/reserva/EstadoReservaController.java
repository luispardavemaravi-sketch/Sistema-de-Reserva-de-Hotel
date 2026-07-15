package pe.edu.utp.sistemadereservacionhotel.controller.reserva;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.sistemadereservacionhotel.dto.reserva.EstadoReservaDTO;
import pe.edu.utp.sistemadereservacionhotel.service.reserva.EstadoReservaService;

import java.util.List;

@RestController
@RequestMapping("/api/reservas/estado")
@RequiredArgsConstructor
public class EstadoReservaController {

    private final EstadoReservaService service;

    @PostMapping
    public ResponseEntity<EstadoReservaDTO> registrar(@RequestBody EstadoReservaDTO dto) {
        return ResponseEntity.ok(service.registrarEstado(dto));
    }

    @GetMapping
    public ResponseEntity<List<EstadoReservaDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstadoReservaDTO> actualizar(@PathVariable Long id, @RequestBody EstadoReservaDTO dto) {
        return ResponseEntity.ok(service.actualizarEstado(id, dto));
    }
}