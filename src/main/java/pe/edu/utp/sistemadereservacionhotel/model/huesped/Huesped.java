package pe.edu.utp.sistemadereservacionhotel.model.huesped;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.Reserva;

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

    @NotBlank(message = "El nombre no puede estar vacío.")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío.")
    @Column(nullable = false, length = 100)
    private String apellidos;

    @NotBlank(message = "El documento de identidad es obligatorio")
    @Column(unique = true, nullable = false, length = 20)
    private String documentoIdentidad;

    @NotBlank(message = "El correo electrónico no puede estar vacío.")
    @Email(message = "El correo electrónico debe ser válido.")
    @Column(unique = true, nullable = false, length = 150)
    private String email;

    @Column(length = 20)
    private String telefono;

    // Un huésped tiene muchas reservas a lo largo del tiempo
    @OneToMany(mappedBy = "huesped", cascade = CascadeType.ALL)
    private List<Reserva> historialReservas;
}
