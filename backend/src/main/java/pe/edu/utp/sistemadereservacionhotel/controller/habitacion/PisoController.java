package pe.edu.utp.sistemadereservacionhotel.controller.habitacion;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.sistemadereservacionhotel.dto.habitacion.PisoDTO;
import pe.edu.utp.sistemadereservacionhotel.service.habitacion.PisoService;

import java.util.List;

@RestController
@RequestMapping("/api/pisos")
@RequiredArgsConstructor
public class PisoController {

    private final PisoService pisoService;

    @PostMapping
    public ResponseEntity<PisoDTO> registrar(@RequestBody PisoDTO dto) {
        return ResponseEntity.ok(pisoService.registrarPiso(dto));
    }

    @GetMapping
    public ResponseEntity<List<PisoDTO>> listarTodos() {
        return ResponseEntity.ok(pisoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PisoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pisoService.buscarPorId(id));
    }

    @GetMapping("/numero/{numero}")
    public ResponseEntity<PisoDTO> buscarPorNumero(@PathVariable Integer numero) {
        return ResponseEntity.ok(pisoService.buscarPorNumero(numero));
    }

    @GetMapping("/sector/{sector}")
    public ResponseEntity<List<PisoDTO>> buscarPorSector(@PathVariable String sector) {
        return ResponseEntity.ok(pisoService.buscarPorSector(sector));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PisoDTO> actualizar(@PathVariable Long id, @RequestBody PisoDTO dto) {
        return ResponseEntity.ok(pisoService.actualizarPiso(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pisoService.eliminarPiso(id);
        return ResponseEntity.noContent().build();
    }
}