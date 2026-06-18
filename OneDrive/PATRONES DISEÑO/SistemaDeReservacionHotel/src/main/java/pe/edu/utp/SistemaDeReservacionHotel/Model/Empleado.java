package pe.edu.utp.SistemaDeReservacionHotel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "empleado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Empleado implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEmpleado;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, length = 150)
    private String apellido;

    @Email(message = "El correo electrónico debe ser válido")
    @Column(nullable = false, length = 150)
    private String email;

    @Column(nullable = false, length = 150)
    private String telefono;

    @Column(nullable = false, length = 150)
    private String cargo;

    @Column(nullable = false, length = 150)
    private String especialidad;

    @Column(nullable = false, length = 150)
    private String direccion;

    @Column(nullable = false, length = 150)
    private String ciudad;
}