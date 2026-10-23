package pe.edu.utp.sistemadereservacionhotel.model.reserva;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entidad que registra el evento físico de llegada y ocupación por parte del huésped.
 * Separa el acto operativo de entrada de la reserva planificada.
 */
@Entity
@Table(name = "check_in")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckIn implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Identificador único del evento de entrada a nivel de base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCheckIn;

    /**
     * Marca de tiempo exacta en la que el huésped tomó posesión de la habitación.
     * Bloqueada para actualizaciones tras su persistencia inicial.
     * Nota técnica: Carece de anotaciones de validación (@NotNull) o autogeneración
     * (@CreationTimestamp), delegando peligrosamente la integridad a la capa de servicio.
     */
    @NotNull(message = "La fecha y hora de entrada es obligatoria")
    @Column(updatable = false)
    private LocalDateTime fechaHoraRealEntrada;

    /**
     * Anotaciones operativas del recepcionista al momento del ingreso.
     * Limitado a 255 caracteres en la persistencia.
     */
    @Column(length = 255)
    private String observaciones;

    /**
     * Vínculo estricto con la reserva que origina este ingreso.
     * Relación uno a uno obligatoria.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reserva", unique = true, nullable = false)
    private Reserva reserva;
}