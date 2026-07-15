package pe.edu.utp.sistemadereservacionhotel.controller.servicio;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.sistemadereservacionhotel.dto.servicio.ConsumoServicioDTO;
import pe.edu.utp.sistemadereservacionhotel.service.servicio.ConsumoServicioService;

import java.util.List;

@RestController
@RequestMapping("/api/servicios/consumo")
@RequiredArgsConstructor
public class ConsumoServicioController {

    private final ConsumoServicioService service;

    @PostMapping
    public ResponseEntity<ConsumoServicioDTO> registrar(@RequestBody ConsumoServicioDTO dto) {
        return ResponseEntity.ok(service.registrarConsumo(dto));
    }

    @GetMapping("/reserva/{idReserva}")
    public ResponseEntity<List<ConsumoServicioDTO>> listarPorReserva(@PathVariable Long idReserva) {
        return ResponseEntity.ok(service.listarPorReserva(idReserva));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsumoServicioDTO> actualizar(@PathVariable Long id, @RequestBody ConsumoServicioDTO dto) {
        return ResponseEntity.ok(service.actualizarConsumo(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminarConsumo(id);
        return ResponseEntity.noContent().build();
    }
}