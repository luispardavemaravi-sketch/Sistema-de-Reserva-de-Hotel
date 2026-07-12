package pe.edu.utp.sistemadereservacionhotel.model.reserva;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "acompanante")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Acompanante implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAcompanante;

    @NotBlank(message = "El nombre completo es obligatorio")
    @Column(nullable = false, length = 150)
    private String nombreCompleto;

    @NotBlank(message = "El documento de identidad es obligatorio")
    @Column(nullable = false, length = 20)
    private String documentoIdentidad;

    @Column(length = 50)
    private String parentesco;

    @ManyToOne
    @JoinColumn(name = "id_reserva", nullable = false)
    private Reserva reserva;
}