package pe.edu.utp.sistemadereservacionhotel.controller.finanzas;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.sistemadereservacionhotel.dto.finanzas.ImpuestoDTO;
import pe.edu.utp.sistemadereservacionhotel.service.finanzas.ImpuestoService;

import java.util.List;

@RestController
@RequestMapping("/api/finanzas/impuesto")
@RequiredArgsConstructor
public class ImpuestoController {

    private final ImpuestoService service;

    @PostMapping
    public ResponseEntity<ImpuestoDTO> crear(@RequestBody ImpuestoDTO dto) {
        return ResponseEntity.ok(service.registrarImpuesto(dto));
    }

    @GetMapping
    public ResponseEntity<List<ImpuestoDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ImpuestoDTO> actualizar(@PathVariable Long id, @RequestBody ImpuestoDTO dto) {
        return ResponseEntity.ok(service.actualizarImpuesto(id, dto));
    }
}