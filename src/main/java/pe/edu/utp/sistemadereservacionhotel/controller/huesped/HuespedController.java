package pe.edu.utp.sistemadereservacionhotel.controller.huesped;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.sistemadereservacionhotel.dto.huesped.HuespedRequestDTO;
import pe.edu.utp.sistemadereservacionhotel.dto.huesped.HuespedResponseDTO;
import pe.edu.utp.sistemadereservacionhotel.service.huesped.HuespedService;

import java.util.List;

@RestController
@RequestMapping("/api/huespedes")
@RequiredArgsConstructor
public class HuespedController {

    private final HuespedService service;

    @PostMapping
    public ResponseEntity<HuespedResponseDTO> registrar(@RequestBody HuespedRequestDTO dto) {
        return ResponseEntity.ok(service.registrarHuesped(dto));
    }

    @GetMapping
    public ResponseEntity<List<HuespedResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HuespedResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<HuespedResponseDTO> buscarPorEmail(@PathVariable String email) {
        return ResponseEntity.ok(service.buscarPorEmail(email));
    }

    @GetMapping("/documento/{documento}")
    public ResponseEntity<HuespedResponseDTO> buscarPorDocumento(@PathVariable String documento) {
        return ResponseEntity.ok(service.buscarPorDocumentoIdentidad(documento));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HuespedResponseDTO> actualizar(@PathVariable Long id, @RequestBody HuespedRequestDTO dto) {
        return ResponseEntity.ok(service.actualizarHuesped(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> darDeBaja(@PathVariable Long id) {
        service.darDeBajaHuesped(id);
        return ResponseEntity.noContent().build();
    }
}