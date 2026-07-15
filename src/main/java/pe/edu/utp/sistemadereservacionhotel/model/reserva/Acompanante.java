package pe.edu.utp.sistemadereservacionhotel.model.reserva;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Entidad de dominio que representa a los individuos adicionales (acompañantes)
 * vinculados a una reserva principal de alojamiento.
 * * Esta entidad permite mantener un registro de aforo por habitación, crucial para
 * fines de seguridad, cumplimiento normativo y control estadístico del hotel.
 */
@Entity
@Table(name = "acompanante")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Acompanante implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Identificador único del acompañante.
     * Clave primaria autoincremental gestionada por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAcompanante;

    /**
     * Nombre(s) del acompañante.
     */
    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false, length = 100)
    private String nombreCompleto;

    /**
     * Apellidos del acompañante.
     */
    @NotBlank(message = "El apellido es obligatorio")
    @Column(nullable = false, length = 100)
    private String apellidos;

    /**
     * Documento nacional de identidad, pasaporte o NIE.
     */
    @NotBlank(message = "El documento de identidad es obligatorio")
    @Column(nullable = false, length = 20)
    private String documentoIdentidad;

    /**
     * Vínculo relacional con el titular de la reserva.
     * Persistido como cadena de texto (String) mediante EnumType.STRING.
     */
    @NotNull(message = "El parentesco es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Parentesco parentesco;

    /**
     * Relación de cardinalidad Muchos-a-Uno con la reserva.
     * Utiliza Lazy Loading para optimizar el rendimiento al cargar la lista de acompañantes.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reserva", nullable = false)
    private Reserva reserva;
}