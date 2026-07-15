package pe.edu.utp.sistemadereservacionhotel.model.personal;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.utp.sistemadereservacionhotel.config.Auditor;

import java.io.Serializable;

@Entity
@Table(name = "empleado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Empleado extends Auditor<String> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEmpleado;

    @NotBlank(message = "El nombre no puede estar vacío.")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío.")
    @Column(nullable = false, length = 100)
    private String apellido;

    @Email(message = "El correo electrónico debe ser válido.")
    @Column(unique = true, nullable = false, length = 150)
    private String email;

    @NotBlank(message = "El teléfono no puede estar vacío.")
    @Column(nullable = false, length = 20) // Reducido a 20 caracteres
    private String telefono;

    @NotBlank(message = "El cargo no puede estar vacío.")
    @Column(nullable = false, length = 100)
    private String cargo;

    @NotBlank(message = "La especialidad no puede estar vacía.")
    @Column(nullable = false, length = 100)
    private String especialidad;

    @NotBlank(message = "La dirección no puede estar vacía.")
    @Column(nullable = false, length = 255) // Aumentado por si las direcciones son largas
    private String direccion;

    @NotBlank(message = "La ciudad no puede estar vacía.")
    @Column(nullable = false, length = 100)
    private String ciudad;
}