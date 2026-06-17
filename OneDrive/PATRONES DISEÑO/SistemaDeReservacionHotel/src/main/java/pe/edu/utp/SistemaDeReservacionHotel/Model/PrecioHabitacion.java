package pe.edu.utp.SistemaDeReservacionHotel.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "precio_habitacion")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PrecioHabitacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPrecio;
    private Double monto;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    @ManyToOne
    @JoinColumn(name = "id_habitacion")
    private Habitacion habitacion;

}
