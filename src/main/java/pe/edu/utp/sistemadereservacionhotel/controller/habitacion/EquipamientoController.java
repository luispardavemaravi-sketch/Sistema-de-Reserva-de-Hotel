package pe.edu.utp.sistemadereservacionhotel.controller.habitacion;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.sistemadereservacionhotel.dto.habitacion.EquipamientoDTO;
import pe.edu.utp.sistemadereservacionhotel.service.habitacion.EquipamientoService;

import java.util.List;

@RestController
@RequestMapping("/api/habitaciones/equipamiento")
@RequiredArgsConstructor
public class EquipamientoController {

    private final EquipamientoService service;

    @PostMapping
    public ResponseEntity<EquipamientoDTO> registrar(@RequestBody EquipamientoDTO dto) {
        return ResponseEntity.ok(service.registrarEquipamiento(dto));
    }

    @GetMapping
    public ResponseEntity<List<EquipamientoDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminarEquipamiento(id);
        return ResponseEntity.noContent().build();
    }
}