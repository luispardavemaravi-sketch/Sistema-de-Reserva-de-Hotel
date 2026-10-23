package pe.edu.utp.sistemadereservacionhotel.controller.servicio;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.sistemadereservacionhotel.dto.servicio.CatalogoServicioDTO;
import pe.edu.utp.sistemadereservacionhotel.service.servicio.CatalogoServicioService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/servicios/catalogo")
@RequiredArgsConstructor
public class CatalogoServicioController {

    private final CatalogoServicioService service;

    @PostMapping
    public ResponseEntity<CatalogoServicioDTO> registrar(@RequestBody CatalogoServicioDTO dto) {
        return ResponseEntity.ok(service.registrarEnCatalogo(dto));
    }

    @GetMapping
    public ResponseEntity<List<CatalogoServicioDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<CatalogoServicioDTO>> buscarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(service.buscarPorCategoria(categoria));
    }

    @GetMapping("/precio-maximo")
    public ResponseEntity<List<CatalogoServicioDTO>> buscarPorPrecio(@RequestParam BigDecimal precioMaximo) {
        return ResponseEntity.ok(service.buscarPorPrecioMaximo(precioMaximo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CatalogoServicioDTO> actualizar(@PathVariable Long id, @RequestBody CatalogoServicioDTO dto) {
        return ResponseEntity.ok(service.actualizarEnCatalogo(id, dto));
    }
}