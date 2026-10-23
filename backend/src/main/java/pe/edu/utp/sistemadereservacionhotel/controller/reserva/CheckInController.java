package pe.edu.utp.sistemadereservacionhotel.controller.reserva;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.sistemadereservacionhotel.dto.reserva.CheckInRequestDTO;
import pe.edu.utp.sistemadereservacionhotel.dto.reserva.CheckInResponseDTO;
import pe.edu.utp.sistemadereservacionhotel.service.reserva.CheckInService;

@RestController
@RequestMapping("/api/reservas/check-in")
@RequiredArgsConstructor
public class CheckInController {

    private final CheckInService service;

    @PostMapping
    public ResponseEntity<CheckInResponseDTO> realizarCheckIn(@RequestBody CheckInRequestDTO dto) {
        return ResponseEntity.ok(service.realizarCheckIn(dto));
    }

    @GetMapping("/reserva/{idReserva}")
    public ResponseEntity<CheckInResponseDTO> buscarPorReserva(@PathVariable Long idReserva) {
        return ResponseEntity.ok(service.buscarPorReserva(idReserva));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CheckInResponseDTO> actualizar(@PathVariable Long id, @RequestBody CheckInRequestDTO dto) {
        return ResponseEntity.ok(service.actualizarCheckIn(id, dto));
    }
}