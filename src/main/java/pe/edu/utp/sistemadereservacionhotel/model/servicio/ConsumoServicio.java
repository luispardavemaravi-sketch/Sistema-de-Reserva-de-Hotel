package pe.edu.utp.sistemadereservacionhotel.model.servicio;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.Reserva;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad transaccional que registra la adquisición o uso de un servicio por parte
 * de un huésped durante su estadía, vinculándolo a su cuenta maestra (Reserva).
 */
@Entity
@Table(name = "consumo_servicio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsumoServicio implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Identificador único del evento de consumo a nivel de base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idConsumo;

    /**
     * Marca de tiempo exacta en la que se registró el cargo.
     */
    @NotNull(message = "La fecha y hora del consumo es obligatoria")
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaHora;

    /**
     * Número de unidades consumidas del servicio.
     */
    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a cero")
    @Column(nullable = false)
    private Integer cantidad;

    /**
     * Costo total de esta línea de consumo.
     */
    @NotNull(message = "El subtotal es obligatorio")
    @PositiveOrZero(message = "El subtotal no puede ser negativo")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subTotal;

    /**
     * Vínculo directo a la reserva (cuenta maestra) a la que se cargará este consumo.
     * Corregido: Se aplicó FetchType.LAZY para aislar el rendimiento.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reserva", nullable = false)
    private Reserva reserva;

    /**
     * Referencia al servicio específico que fue consumido.
     * Corregido: Se aplicó FetchType.LAZY.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_servicio", nullable = false)
    private ServicioAdicional servicioAdicional;
}