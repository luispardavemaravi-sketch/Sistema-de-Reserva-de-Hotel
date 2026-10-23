package pe.edu.utp.sistemadereservacionhotel.controller.personal;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.sistemadereservacionhotel.dto.personal.EmpleadoRequestDTO;
import pe.edu.utp.sistemadereservacionhotel.dto.personal.EmpleadoResponseDTO;
import pe.edu.utp.sistemadereservacionhotel.model.personal.CargoEmpleado;
import pe.edu.utp.sistemadereservacionhotel.service.personal.EmpleadoService;

import java.util.List;

@RestController
@RequestMapping("/api/personal/empleados")
@RequiredArgsConstructor
public class EmpleadoController {

    private final EmpleadoService service;

    @PostMapping
    public ResponseEntity<EmpleadoResponseDTO> registrar(@RequestBody EmpleadoRequestDTO dto) {
        return ResponseEntity.ok(service.registrarEmpleado(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpleadoResponseDTO> actualizar(@PathVariable Long id, @RequestBody EmpleadoRequestDTO dto) {
        return ResponseEntity.ok(service.actualizarEmpleado(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> darDeBaja(@PathVariable Long id) {
        service.darDeBajaEmpleado(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<EmpleadoResponseDTO>> listarActivos() {
        return ResponseEntity.ok(service.listarActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<EmpleadoResponseDTO> buscarPorEmail(@PathVariable String email) {
        return ResponseEntity.ok(service.buscarPorEmail(email));
    }

    @GetMapping("/cargo/{cargo}")
    public ResponseEntity<List<EmpleadoResponseDTO>> buscarPorCargo(@PathVariable CargoEmpleado cargo) {
        return ResponseEntity.ok(service.buscarPorCargo(cargo));
    }
}