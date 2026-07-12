package pe.edu.utp.sistemadereservacionhotel.model.finanzas;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "detalle_comprobante")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleComprobante implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalle;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a cero")
    @Column(nullable = false)
    private Integer cantidad;

    @NotBlank(message = "La descripción es obligatoria")
    @Column(nullable = false, length = 255)
    private String descripcion;

    @NotNull(message = "El precio unitario es obligatorio")
    @Positive(message = "El precio unitario debe ser mayor a cero")
    @Column(nullable = false)
    private Double precioUnitario;

    @PositiveOrZero(message = "El subtotal no puede ser negativo")
    @Column(nullable = false)
    private Double subtotalLinea;

    @ManyToOne
    @JoinColumn(name = "id_comprobante", nullable = false)
    private ComprobantePago comprobantePago;
}