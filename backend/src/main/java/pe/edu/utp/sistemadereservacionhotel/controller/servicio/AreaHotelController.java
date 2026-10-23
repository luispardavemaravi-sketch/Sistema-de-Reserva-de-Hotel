package pe.edu.utp.sistemadereservacionhotel.controller.servicio;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.sistemadereservacionhotel.dto.servicio.AreaHotelDTO;
import pe.edu.utp.sistemadereservacionhotel.service.servicio.AreaHotelService;

import java.util.List;

@RestController
@RequestMapping("/api/servicios/areas")
@RequiredArgsConstructor
public class AreaHotelController {

    private final AreaHotelService service;

    @PostMapping
    public ResponseEntity<AreaHotelDTO> registrar(@RequestBody AreaHotelDTO dto) {
        return ResponseEntity.ok(service.registrarArea(dto));
    }

    @GetMapping
    public ResponseEntity<List<AreaHotelDTO>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AreaHotelDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AreaHotelDTO> actualizar(@PathVariable Long id, @RequestBody AreaHotelDTO dto) {
        return ResponseEntity.ok(service.actualizarArea(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminarArea(id);
        return ResponseEntity.noContent().build();
    }
}