package pe.edu.utp.sistemadereservacionhotel.controller.servicio;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.sistemadereservacionhotel.dto.servicio.ServicioAdicionalDTO;
import pe.edu.utp.sistemadereservacionhotel.service.servicio.ServicioAdicionalService;

import java.util.List;

@RestController
@RequestMapping("/api/servicios/adicionales")
@RequiredArgsConstructor
public class ServicioAdicionalController {

    private final ServicioAdicionalService service;

    @PostMapping
    public ResponseEntity<ServicioAdicionalDTO> registrar(@RequestBody ServicioAdicionalDTO dto) {
        return ResponseEntity.ok(service.registrarServicio(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicioAdicionalDTO> actualizar(@PathVariable Long id, @RequestBody ServicioAdicionalDTO dto) {
        return ResponseEntity.ok(service.actualizarServicio(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminarServicio(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ServicioAdicionalDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicioAdicionalDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<ServicioAdicionalDTO> buscarPorNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(service.buscarPorNombre(nombre));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ServicioAdicionalDTO>> buscarPorNombreContiene(@RequestParam String nombre) {
        return ResponseEntity.ok(service.buscarPorNombreContiene(nombre));
    }

    @GetMapping("/disponibilidad")
    public ResponseEntity<List<ServicioAdicionalDTO>> buscarPorDisponibilidad(@RequestParam Boolean disponible) {
        return ResponseEntity.ok(service.buscarPorDisponibilidad(disponible));
    }
}