package pe.edu.utp.sistemadereservacionhotel.controller.finanzas;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
// IMPORTANTE: Cambia el import a ResponseDTO
import pe.edu.utp.sistemadereservacionhotel.dto.finanzas.DetalleComprobanteResponseDTO;
import pe.edu.utp.sistemadereservacionhotel.service.finanzas.DetalleComprobanteService;

import java.util.List;

@RestController
@RequestMapping("/api/finanzas/detalle-comprobante")
@RequiredArgsConstructor
public class DetalleComprobanteController {

    private final DetalleComprobanteService service;

    @GetMapping("/comprobante/{idComprobante}")
    public ResponseEntity<List<DetalleComprobanteResponseDTO>> listarPorComprobante(@PathVariable Long idComprobante) {
        // Asegúrate de que el método en la interfaz sea buscarPorComprobante o listarPorComprobante según tu Service
        return ResponseEntity.ok(service.buscarPorComprobante(idComprobante));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleComprobanteResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }
}