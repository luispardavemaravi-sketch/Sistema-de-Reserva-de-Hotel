package pe.edu.utp.sistemadereservacionhotel.model.habitacion;

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
@Table(name = "tipo_habitacion")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TipoHabitacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTipo;

    @NotBlank(message = "El nombre no debe estar vacio.")
    @Column(unique = true, nullable = false, length = 100)
    private String nombre;

    @NotNull(message = "La capacidad máxima es obligatoria.")
    @Column(nullable = false)
    @Positive(message = "La capacidad máxima debe ser un valor positivo.")
    private Integer capacidadMaxima;

    @PositiveOrZero(message = "El precio base no puede estar vacio.")
    @Column(nullable = false)
    private Double precioBase;

}
