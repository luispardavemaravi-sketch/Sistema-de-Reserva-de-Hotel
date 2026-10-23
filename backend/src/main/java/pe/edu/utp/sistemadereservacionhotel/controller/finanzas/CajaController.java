package pe.edu.utp.sistemadereservacionhotel.controller.finanzas;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.sistemadereservacionhotel.dto.finanzas.CajaAperturaDTO;
import pe.edu.utp.sistemadereservacionhotel.dto.finanzas.CajaResponseDTO;
import pe.edu.utp.sistemadereservacionhotel.service.finanzas.CajaService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/finanzas/caja")
@RequiredArgsConstructor
public class CajaController {

    private final CajaService cajaService;

    // Se cambió a 'abrirCaja' para coincidir con tu Service
    @PostMapping("/abrir")
    public ResponseEntity<CajaResponseDTO> abrir(@RequestBody CajaAperturaDTO dto) {
        return ResponseEntity.ok(cajaService.abrirCaja(dto));
    }

    // Se cambió a 'cerrarCaja' y recibe el monto como parámetro
    @PutMapping("/{id}/cerrar")
    public ResponseEntity<CajaResponseDTO> cerrar(@PathVariable Long id, @RequestParam BigDecimal montoCierre) {
        return ResponseEntity.ok(cajaService.cerrarCaja(id, montoCierre));
    }

    @GetMapping
    public ResponseEntity<List<CajaResponseDTO>> listarTodos() {
        return ResponseEntity.ok(cajaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CajaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(cajaService.buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        cajaService.eliminarCaja(id);
        return ResponseEntity.noContent().build();
    }
}