package pe.edu.utp.sistemadereservacionhotel.model.finanzas;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.Reserva;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "comprobante_pago")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComprobantePago implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idComprobante;

    @NotBlank(message = "El número de serie es obligatorio")
    @Column(unique = true, nullable = false, length = 20)
    private String numeroSerie;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaEmision;

    @NotNull(message = "El monto total es obligatorio")
    @PositiveOrZero(message = "El monto total no puede ser negativo")
    @Column(nullable = false)
    private Double montoTotal;

    @OneToOne
    @JoinColumn(name = "id_reserva", nullable = false)
    private Reserva reserva;

    @OneToMany(mappedBy = "comprobantePago", cascade = CascadeType.ALL)
    private List<DetalleComprobante> detalles;
}