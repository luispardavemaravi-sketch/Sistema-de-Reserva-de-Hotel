package pe.edu.utp.sistemadereservacionhotel.model.habitacion;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "equipamiento")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Equipamiento implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEquipamiento;

    @NotBlank(message = "El nombre del equipamiento es obligatorio")
    @Column(unique = true, nullable = false, length = 100)
    private String nombre;

    @PositiveOrZero(message = "El costo adicional no puede ser negativo")
    @Column(nullable = false)
    private Double costoAdicional = 0.0;  // valor por defecto 0
}