package pe.edu.utp.SistemaDeReservacionHotel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
    @Column(nullable = false, length = 100)
    private String nombre;


    private Integer capacidadMaxima;

    @PositiveOrZero(message = "El precio base no puede estar vacio.")
    @Column(unique = true, nullable = false, length = 100)
    private Double precioBase;

}
