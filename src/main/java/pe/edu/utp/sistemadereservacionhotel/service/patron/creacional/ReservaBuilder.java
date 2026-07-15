package pe.edu.utp.sistemadereservacionhotel.service.patron.creacional;

import pe.edu.utp.sistemadereservacionhotel.model.reserva.Reserva;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.EstadoReserva;
import pe.edu.utp.sistemadereservacionhotel.model.huesped.Huesped;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ReservaBuilder {

    private String codigoReserva;
    private LocalDateTime fechaReserva;
    private LocalDate fechaEntradaPlanificada;
    private Double montoTotalEstimado;
    private Huesped huesped;
    private EstadoReserva estadoReserva;

    // Constructor privado — solo se crea desde builder()
    private ReservaBuilder() {
    }

    // Punto de entrada
    public static ReservaBuilder builder() {
        return new ReservaBuilder();
    }

    public ReservaBuilder codigoReserva(String codigoReserva) {
        if (codigoReserva == null || codigoReserva.isBlank()) {
            throw new IllegalArgumentException("El código de reserva no puede estar vacío");
        }
        this.codigoReserva = codigoReserva;
        return this;
    }

    public ReservaBuilder fechaEntradaPlanificada(LocalDate fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha de entrada no puede ser nula");
        }
        if (fecha.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de entrada no puede ser anterior a hoy");
        }
        this.fechaEntradaPlanificada = fecha;
        return this;
    }

    public ReservaBuilder montoTotalEstimado(Double monto) {
        if (monto == null || monto < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo");
        }
        this.montoTotalEstimado = monto;
        return this;
    }

    public ReservaBuilder huesped(Huesped huesped) {
        if (huesped == null) {
            throw new IllegalArgumentException("El huésped no puede ser nulo");
        }
        this.huesped = huesped;
        return this;
    }

    public ReservaBuilder estadoReserva(EstadoReserva estadoReserva) {
        if (estadoReserva == null) {
            throw new IllegalArgumentException("El estado de reserva no puede ser nulo");
        }
        this.estadoReserva = estadoReserva;
        return this;
    }

    public Reserva build() {
        // Validar campos obligatorios
        if (codigoReserva == null) throw new IllegalStateException("El código de reserva es obligatorio");
        if (fechaEntradaPlanificada == null) throw new IllegalStateException("La fecha de entrada es obligatoria");
        if (huesped == null) throw new IllegalStateException("El huésped es obligatorio");
        if (estadoReserva == null) throw new IllegalStateException("El estado de reserva es obligatorio");

        Reserva reserva = new Reserva();
        reserva.setCodigoReserva(codigoReserva);
        reserva.setFechaReserva(LocalDateTime.now()); // se asigna automáticamente
        reserva.setFechaEntradaPlanificada(fechaEntradaPlanificada);
        reserva.setMontoTotalEstimado(montoTotalEstimado != null ? montoTotalEstimado : 0.0);
        reserva.setHuesped(huesped);
        reserva.setEstadoReserva(estadoReserva);

        return reserva;
    }
}