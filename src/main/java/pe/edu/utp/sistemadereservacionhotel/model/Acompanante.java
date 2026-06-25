package pe.edu.utp.sistemadereservacionhotel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "acompanante") // Sin 'ñ' en nombres de tabla por estándar de BD
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Acompanante implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAcompanante;

    private String nombreCompleto;
    private String documentoIdentidad;
    private String parentesco;

    // Relación N a 1 hacia Reserva
    @ManyToOne
    @JoinColumn(name = "id_reserva", nullable = false)
    private Reserva reserva;
}