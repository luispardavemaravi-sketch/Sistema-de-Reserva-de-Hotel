package pe.edu.utp.sistemadereservacionhotel.controller.personal;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.sistemadereservacionhotel.dto.personal.TurnoDTO;
import pe.edu.utp.sistemadereservacionhotel.service.personal.TurnoService;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/personal/turnos")
@RequiredArgsConstructor
public class TurnoController {

    private final TurnoService service;

    @PostMapping
    public ResponseEntity<TurnoDTO> registrar(@RequestBody TurnoDTO dto) {
        return ResponseEntity.ok(service.registrarTurno(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TurnoDTO> actualizar(@PathVariable Long id, @RequestBody TurnoDTO dto) {
        return ResponseEntity.ok(service.actualizarTurno(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminarTurno(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<TurnoDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TurnoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/dia/{dia}")
    public ResponseEntity<List<TurnoDTO>> buscarPorDia(@PathVariable DayOfWeek dia) {
        return ResponseEntity.ok(service.buscarPorDia(dia));
    }

    @GetMapping("/activo-en/{hora}")
    public ResponseEntity<List<TurnoDTO>> buscarPorHora(@PathVariable String hora) {
        // Se usa String para recibir el formato "HH:mm" y parsearlo internamente
        return ResponseEntity.ok(service.buscarTurnosActivosEnHora(LocalTime.parse(hora)));
    }
}