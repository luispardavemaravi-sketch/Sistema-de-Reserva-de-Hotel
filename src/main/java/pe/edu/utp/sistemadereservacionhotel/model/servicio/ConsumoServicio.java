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
import java.time.LocalDateTime;

@Entity
@Table(name = "consumo_servicio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsumoServicio implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idConsumo;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaHora;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a cero")
    @Column(nullable = false)
    private Integer cantidad;

    @PositiveOrZero(message = "El subtotal no puede ser negativo")
    @Column(nullable = false)
    private Double subTotal;

    @ManyToOne
    @JoinColumn(name = "id_reserva", nullable = false)
    private Reserva reserva;

    @ManyToOne
    @JoinColumn(name = "id_servicio", nullable = false)
    private ServicioAdicional servicioAdicional;
}