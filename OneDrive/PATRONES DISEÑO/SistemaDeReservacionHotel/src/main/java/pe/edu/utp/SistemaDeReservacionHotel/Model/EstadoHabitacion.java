package pe.edu.utp.sistemadereservacionhotel.model;

import jakarta.persistence.*;
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
    private String nombreEstado;
    private Boolean esReservable;

}
