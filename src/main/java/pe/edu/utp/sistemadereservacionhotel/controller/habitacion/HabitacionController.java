package pe.edu.utp.sistemadereservacionhotel.controller.habitacion;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.sistemadereservacionhotel.dto.habitacion.HabitacionRequestDTO;
import pe.edu.utp.sistemadereservacionhotel.dto.habitacion.HabitacionResponseDTO;
import pe.edu.utp.sistemadereservacionhotel.service.habitacion.HabitacionService;

import java.util.List;

@RestController
@RequestMapping("/api/habitaciones")
@RequiredArgsConstructor
public class HabitacionController {

    private final HabitacionService habitacionService;

    @PostMapping
    public ResponseEntity<HabitacionResponseDTO> registrar(@RequestBody HabitacionRequestDTO dto) {
        return ResponseEntity.ok(habitacionService.registrarHabitacion(dto));
    }

    @GetMapping
    public ResponseEntity<List<HabitacionResponseDTO>> listarTodas() {
        return ResponseEntity.ok(habitacionService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HabitacionResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(habitacionService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HabitacionResponseDTO> actualizar(@PathVariable Long id, @RequestBody HabitacionRequestDTO dto) {
        return ResponseEntity.ok(habitacionService.actualizarHabitacion(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> darDeBaja(@PathVariable Long id) {
        habitacionService.darDeBajaHabitacion(id);
        return ResponseEntity.noContent().build();
    }
}