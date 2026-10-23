package pe.edu.utp.sistemadereservacionhotel.controller.reserva;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.sistemadereservacionhotel.dto.reserva.CheckOutRequestDTO;
import pe.edu.utp.sistemadereservacionhotel.dto.reserva.CheckOutResponseDTO;
import pe.edu.utp.sistemadereservacionhotel.service.reserva.CheckOutService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/reservas/check-out")
@RequiredArgsConstructor
public class CheckOutController {

    private final CheckOutService service;

    @PostMapping
    public ResponseEntity<CheckOutResponseDTO> realizarCheckOut(@RequestBody CheckOutRequestDTO dto) {
        return ResponseEntity.ok(service.realizarCheckOut(dto));
    }

    @GetMapping("/reserva/{idReserva}")
    public ResponseEntity<CheckOutResponseDTO> buscarPorReserva(@PathVariable Long idReserva) {
        return ResponseEntity.ok(service.buscarPorReserva(idReserva));
    }

    @GetMapping("/multas")
    public ResponseEntity<List<CheckOutResponseDTO>> buscarConMulta(@RequestParam BigDecimal montoMinimo) {
        return ResponseEntity.ok(service.buscarConMulta(montoMinimo));
    }
}