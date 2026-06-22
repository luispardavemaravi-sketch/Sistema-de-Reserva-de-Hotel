package pe.edu.utp.SistemaDeReservacionHotel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "estado_habitacion")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EstadoHabitacion implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEstado;

    @NotBlank(message = "El nombre del estado es obligatorio")
    @Column(unique = true, nullable = false, length = 40)
    private String nombreEstado;

    @NotNull(message = "Debe indicar si el estado es reservable")
    @Column(nullable = false)
    private Boolean esReservable;
}