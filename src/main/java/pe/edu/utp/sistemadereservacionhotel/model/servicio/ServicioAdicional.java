package pe.edu.utp.sistemadereservacionhotel.model.servicio;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "servicio_adicional")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServicioAdicional implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idServicio;

    @NotBlank(message = "El nombre del servicio es obligatorio")
    @Column(unique = true, nullable = false, length = 100)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    @NotNull(message = "El precio unitario es obligatorio")
    @PositiveOrZero(message = "El precio unitario debe ser positivo o cero")
    @Column(nullable = false)
    private Double precioUnitario;

    @NotNull(message = "Debe indicar si el servicio está disponible")
    @Column(nullable = false)
    private Boolean estaDisponible = true;
}