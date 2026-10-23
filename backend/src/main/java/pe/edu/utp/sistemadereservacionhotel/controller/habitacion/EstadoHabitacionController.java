package pe.edu.utp.sistemadereservacionhotel.controller.habitacion;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.sistemadereservacionhotel.dto.habitacion.EstadoHabitacionDTO;
import pe.edu.utp.sistemadereservacionhotel.service.habitacion.EstadoHabitacionService;

import java.util.List;

@RestController
@RequestMapping("/api/habitaciones/estado")
@RequiredArgsConstructor
public class EstadoHabitacionController {

    private final EstadoHabitacionService service;

    @PostMapping
    public ResponseEntity<EstadoHabitacionDTO> registrar(@RequestBody EstadoHabitacionDTO dto) {
        return ResponseEntity.ok(service.registrar(dto));
    }

    @GetMapping
    public ResponseEntity<List<EstadoHabitacionDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstadoHabitacionDTO> actualizar(@PathVariable Long id, @RequestBody EstadoHabitacionDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }
}