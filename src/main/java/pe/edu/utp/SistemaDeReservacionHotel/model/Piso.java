package pe.edu.utp.SistemaDeReservacionHotel.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "piso")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Piso implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPiso;
    private Integer numeroPiso;
    private String sector;

    // un piso puede tener muchas habitaciones
    @OneToMany(mappedBy = "piso", cascade = CascadeType.ALL)
    private List<Habitacion> habitaciones;
}
