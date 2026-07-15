package pe.edu.utp.sistemadereservacionhotel.service.patron.creacional;

import pe.edu.utp.sistemadereservacionhotel.model.reserva.Reserva;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.EstadoReserva;
import pe.edu.utp.sistemadereservacionhotel.model.habitacion.Habitacion;
import pe.edu.utp.sistemadereservacionhotel.model.huesped.Huesped;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ReservaBuilder {

    private String codigoReserva;
    private LocalDate fechaEntradaPlanificada;
    private LocalDate fechaSalidaPlanificada;
    private BigDecimal montoTotalEstimado;
    private Huesped huesped;
    private Habitacion habitacion;
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

    public ReservaBuilder fechaSalidaPlanificada(LocalDate fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha de salida no puede ser nula");
        }
        if (fecha.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de salida no puede ser anterior a hoy");
        }
        this.fechaSalidaPlanificada = fecha;
        return this;
    }

    public ReservaBuilder montoTotalEstimado(BigDecimal monto) {
        if (monto == null || monto.signum() < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo ni nulo");
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

    public ReservaBuilder habitacion(Habitacion habitacion) {
        if (habitacion == null) {
            throw new IllegalArgumentException("La habitación no puede ser nula");
        }
        this.habitacion = habitacion;
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
        // Validar campos obligatorios finales
        if (codigoReserva == null) throw new IllegalStateException("El código de reserva es obligatorio");
        if (fechaEntradaPlanificada == null) throw new IllegalStateException("La fecha de entrada es obligatoria");
        if (fechaSalidaPlanificada == null) throw new IllegalStateException("La fecha de salida es obligatoria");
        if (huesped == null) throw new IllegalStateException("El huésped es obligatorio");
        if (habitacion == null) throw new IllegalStateException("La habitación es obligatoria");
        if (estadoReserva == null) throw new IllegalStateException("El estado de reserva es obligatorio");

        if (fechaSalidaPlanificada.isBefore(fechaEntradaPlanificada)) {
            throw new IllegalStateException("La fecha de salida no puede ser anterior a la entrada");
        }

        Reserva reserva = new Reserva();
        reserva.setCodigoReserva(codigoReserva);
        reserva.setFechaReserva(LocalDateTime.now()); // Generación automática
        reserva.setFechaEntradaPlanificada(fechaEntradaPlanificada);
        reserva.setFechaSalidaPlanificada(fechaSalidaPlanificada);
        reserva.setMontoTotalEstimado(montoTotalEstimado != null ? montoTotalEstimado : BigDecimal.ZERO);
        reserva.setHuesped(huesped);
        reserva.setHabitacion(habitacion);
        reserva.setEstadoReserva(estadoReserva);

        return reserva;
    }
}