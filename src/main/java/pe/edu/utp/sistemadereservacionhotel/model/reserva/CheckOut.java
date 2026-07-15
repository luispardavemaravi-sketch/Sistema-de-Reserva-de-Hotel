package pe.edu.utp.sistemadereservacionhotel.model.reserva;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad que registra el evento físico de salida y liberación de la habitación.
 * Contabiliza penalidades y cierra el ciclo de vida operativo de la reserva.
 */
@Entity
@Table(name = "check_out")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckOut implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Identificador único del evento de salida a nivel de base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCheckOut;

    /**
     * Marca de tiempo exacta en la que el huésped entregó la habitación.
     * Bloqueada para modificaciones posteriores.
     */
    @NotNull(message = "La fecha y hora de salida es obligatoria")
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaHoraRealSalida;

    /**
     * Cargo financiero extra aplicado si el huésped excede el límite de la hora de salida.
     * Nota técnica crítica: El uso de Double para valores monetarios viola los
     * estándares de precisión financiera. Se exige refactorización a BigDecimal.
     */
    @NotNull(message = "La multa no puede ser nula")
    @PositiveOrZero(message = "La multa no puede ser negativa")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal multaPorRetraso = BigDecimal.ZERO;

    /**
     * Vínculo directo con la reserva que se está clausurando.
     * Relación uno a uno obligatoria.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reserva", unique = true, nullable = false)
    private Reserva reserva;
}