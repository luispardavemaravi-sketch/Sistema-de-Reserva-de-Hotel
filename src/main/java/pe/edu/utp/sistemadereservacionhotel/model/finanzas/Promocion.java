package pe.edu.utp.sistemadereservacionhotel.model.finanzas;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "promocion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Promocion implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPromocion;

    @NotBlank(message = "El código de cupón es obligatorio")
    @Column(unique = true, nullable = false, length = 20)
    private String codigoCupon;

    @NotNull(message = "El porcentaje de descuento es obligatorio")
    @PositiveOrZero(message = "El descuento no puede ser negativo")
    @Column(nullable = false)
    private Double porcentajeDescuento;

    @NotNull(message = "La fecha de caducidad es obligatoria")
    @Column(nullable = false)
    private LocalDate fechaCaducidad;
}