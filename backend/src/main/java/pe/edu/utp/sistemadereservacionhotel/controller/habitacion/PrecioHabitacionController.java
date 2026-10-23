package pe.edu.utp.sistemadereservacionhotel.controller.habitacion;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.sistemadereservacionhotel.dto.habitacion.PrecioHabitacionDTO;
import pe.edu.utp.sistemadereservacionhotel.service.habitacion.PrecioHabitacionService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/habitaciones/precios")
@RequiredArgsConstructor
public class PrecioHabitacionController {

    private final PrecioHabitacionService service;

    @PostMapping
    public ResponseEntity<PrecioHabitacionDTO> establecerTarifa(@RequestBody PrecioHabitacionDTO dto) {
        return ResponseEntity.ok(service.establecerTarifa(dto));
    }

    @GetMapping("/habitacion/{idHabitacion}")
    public ResponseEntity<List<PrecioHabitacionDTO>> listarHistorial(@PathVariable Long idHabitacion) {
        return ResponseEntity.ok(service.listarHistorialPorHabitacion(idHabitacion));
    }

    @GetMapping("/rango-fecha")
    public ResponseEntity<List<PrecioHabitacionDTO>> buscarPorRangoFecha(
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fin) {
        return ResponseEntity.ok(service.buscarPorRangoFecha(inicio, fin));
    }

    @GetMapping("/rango-monto")
    public ResponseEntity<List<PrecioHabitacionDTO>> buscarPorRangoMonto(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max) {
        return ResponseEntity.ok(service.buscarPorRangoMonto(min, max));
    }
}