package pe.edu.utp.SistemaDeReservacionHotel.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "huesped")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Huesped implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHuesped;

    private String nombre;
    private String apellidos;
    private String documentoIdentidad;
    private String email;
    private String telefono;

    // Un huésped tiene muchas reservas a lo largo del tiempo
    @OneToMany(mappedBy = "huesped", cascade = CascadeType.ALL)
    private List<Reserva> historialReservas;
}
