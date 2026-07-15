package pe.edu.utp.sistemadereservacionhotel.controller.habitacion;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.sistemadereservacionhotel.dto.habitacion.TipoHabitacionDTO;
import pe.edu.utp.sistemadereservacionhotel.service.habitacion.TipoHabitacionService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/habitaciones/tipo")
@RequiredArgsConstructor
public class TipoHabitacionController {

    private final TipoHabitacionService service;

    @PostMapping
    public ResponseEntity<TipoHabitacionDTO> registrar(@RequestBody TipoHabitacionDTO dto) {
        return ResponseEntity.ok(service.registrarTipo(dto));
    }

    @GetMapping
    public ResponseEntity<List<TipoHabitacionDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoHabitacionDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/rango-precio")
    public ResponseEntity<List<TipoHabitacionDTO>> buscarPorRangoPrecio(
            @RequestParam BigDecimal precioMin,
            @RequestParam BigDecimal precioMax) {
        return ResponseEntity.ok(service.buscarPorRangoPrecio(precioMin, precioMax));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoHabitacionDTO> actualizar(@PathVariable Long id, @RequestBody TipoHabitacionDTO dto) {
        return ResponseEntity.ok(service.actualizarTipo(id, dto));
    }
}