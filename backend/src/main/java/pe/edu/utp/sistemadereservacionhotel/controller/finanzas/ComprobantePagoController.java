package pe.edu.utp.sistemadereservacionhotel.controller.finanzas;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.sistemadereservacionhotel.dto.finanzas.ComprobanteRequestDTO;
import pe.edu.utp.sistemadereservacionhotel.dto.finanzas.ComprobanteResponseDTO;
import pe.edu.utp.sistemadereservacionhotel.service.finanzas.ComprobantePagoService;

import java.util.List;

@RestController
@RequestMapping("/api/finanzas/comprobante")
@RequiredArgsConstructor
public class ComprobantePagoController {

    private final ComprobantePagoService service;

    @PostMapping
    public ResponseEntity<ComprobanteResponseDTO> emitir(@RequestBody ComprobanteRequestDTO dto) {
        return ResponseEntity.ok(service.emitirComprobante(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComprobanteResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<ComprobanteResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> anular(@PathVariable Long id) {
        service.anularComprobante(id);
        return ResponseEntity.noContent().build();
    }
}