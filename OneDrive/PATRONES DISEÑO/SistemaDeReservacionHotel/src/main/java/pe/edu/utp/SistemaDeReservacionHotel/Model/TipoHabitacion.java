package pe.edu.utp.SistemaDeReservacionHotel.Model;

import jakarta.persistence.*;
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
    private String nombre;
    private Integer capacidadMaxima;
    private Double precioBase;

}
