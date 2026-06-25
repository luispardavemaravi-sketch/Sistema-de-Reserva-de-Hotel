package pe.edu.utp.sistemadereservacionhotel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "El número de piso es obligatorio")
    @Column(unique = true, nullable = false)
    private Integer numeroPiso;

    @Column(length = 50, nullable = false)
    private String sector;

    // un piso puede tener muchas habitaciones
    @OneToMany(mappedBy = "piso", cascade = CascadeType.ALL)
    private List<Habitacion> habitaciones;
}
