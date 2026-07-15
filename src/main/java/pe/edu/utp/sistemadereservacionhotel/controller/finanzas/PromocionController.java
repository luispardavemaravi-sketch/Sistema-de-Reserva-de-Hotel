package pe.edu.utp.sistemadereservacionhotel.controller.finanzas;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.sistemadereservacionhotel.dto.finanzas.PromocionDTO;
import pe.edu.utp.sistemadereservacionhotel.service.finanzas.PromocionService;

import java.util.List;

@RestController
@RequestMapping("/api/finanzas/promocion")
@RequiredArgsConstructor
public class PromocionController {

    private final PromocionService service;

    @PostMapping
    public ResponseEntity<PromocionDTO> crear(@RequestBody PromocionDTO dto) {
        return ResponseEntity.ok(service.registrarPromocion(dto));
    }

    @GetMapping
    public ResponseEntity<List<PromocionDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PromocionDTO> actualizar(@PathVariable Long id, @RequestBody PromocionDTO dto) {
        return ResponseEntity.ok(service.actualizarPromocion(id, dto));
    }
}