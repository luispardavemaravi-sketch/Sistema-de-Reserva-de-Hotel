package pe.edu.utp.sistemadereservacionhotel.controller.finanzas;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.sistemadereservacionhotel.dto.finanzas.MetodoPagoDTO;
import pe.edu.utp.sistemadereservacionhotel.service.finanzas.MetodoPagoService;

import java.util.List;

@RestController
@RequestMapping("/api/finanzas/metodo-pago")
@RequiredArgsConstructor
public class MetodoPagoController {

    private final MetodoPagoService service;

    @PostMapping
    public ResponseEntity<MetodoPagoDTO> crear(@RequestBody MetodoPagoDTO dto) {
        return ResponseEntity.ok(service.registrarMetodoPago(dto));
    }

    @GetMapping
    public ResponseEntity<List<MetodoPagoDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MetodoPagoDTO> actualizar(@PathVariable Long id, @RequestBody MetodoPagoDTO dto) {
        return ResponseEntity.ok(service.actualizarMetodoPago(id, dto));
    }
}