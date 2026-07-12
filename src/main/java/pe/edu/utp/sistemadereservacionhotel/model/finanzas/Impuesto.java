package pe.edu.utp.sistemadereservacionhotel.model.finanzas;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "impuesto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Impuesto implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idImpuesto;

    @NotBlank(message = "El nombre del impuesto es obligatorio")
    @Column(unique = true, nullable = false, length = 50)
    private String nombreImpuesto;

    @NotNull(message = "El porcentaje es obligatorio")
    @PositiveOrZero(message = "El porcentaje no puede ser negativo")
    @Column(nullable = false)
    private Double porcentaje;
}