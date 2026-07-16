package pe.edu.utp.sistemadereservacionhotel.controller.reserva;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.Reserva;
import pe.edu.utp.sistemadereservacionhotel.repository.reserva.ReservaRepository;
import pe.edu.utp.sistemadereservacionhotel.service.patron.estructural.*;
import pe.edu.utp.sistemadereservacionhotel.service.patron.exception.RecursoNoEncontradoException;

@RestController
@RequestMapping("/api/reservas/{id}/costo")
@RequiredArgsConstructor
public class ReservaCostoController {

    private final ReservaRepository reservaRepository;

    @PostMapping
    public ResponseEntity<CostoReservaResponse> calcularCosto(
            @PathVariable Long id,
            @RequestBody ServiciosAdicionalesRequest request) {

        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Reserva", id));

        ReservaDecorator reservaDecorator = new ReservaBase(reserva);

        if (Boolean.TRUE.equals(request.desayuno())) {
            reservaDecorator = new DesayunoDecorator(reservaDecorator);
        }
        if (Boolean.TRUE.equals(request.spa())) {
            reservaDecorator = new SpaDecorator(reservaDecorator);
        }
        if (Boolean.TRUE.equals(request.estacionamiento())) {
            reservaDecorator = new EstacionamientoDecorator(reservaDecorator);
        }

        return ResponseEntity.ok(new CostoReservaResponse(
                reservaDecorator.obtenerDescripcion(),
                reservaDecorator.calcularCostoTotal()
        ));
    }
}

record ServiciosAdicionalesRequest(Boolean desayuno, Boolean spa, Boolean estacionamiento) {}
record CostoReservaResponse(String descripcion, double costoTotal) {}