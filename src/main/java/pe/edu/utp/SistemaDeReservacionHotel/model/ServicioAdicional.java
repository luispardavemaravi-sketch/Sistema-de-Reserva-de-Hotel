package pe.edu.utp.SistemaDeReservacionHotel.model;

import jakarta.persistence.*;
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

    @NotNull(message = "El nombre del servicio es obligatorio")
    @Column(unique = true, nullable = false, length = 100)
    private String nombre;

    @Column(unique = true, nullable = false, length = 100)
    private String descripcion;

    @PositiveOrZero(message = "El precio unitario debe ser un valor positivo o cero")
    @Column(unique = true, nullable = false, length = 100)
    private Double precioUnitario;


    private Boolean estaDisponible;
}