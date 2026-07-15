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
 * Entidad que registra a los individuos adicionales asociados a una reserva principal.
 * Permite mantener un control de identidad y aforo por habitación para fines de
 * seguridad y cumplimiento normativo del hotel.
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
     * Identificador único del acompañante a nivel de base de datos.
     * Clave primaria gestionada automáticamente por la estrategia de identidad.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAcompanante;

    /**
     * Nombre(s) y apellidos del acompañante consolidados en un solo campo.
     * Restricción obligatoria, limitada a 150 caracteres en esquema DDL.
     * Nota técnica de diseño: La falta de normalización hacia una entidad central de
     * personas provocará redundancia de datos si el visitante es recurrente.
     */
    @NotBlank(message = "El nombre completo es obligatorio")
    @Column(nullable = false, length = 150)
    private String nombreCompleto;

    /**
     * Número de documento oficial de identidad (DNI, Pasaporte, NIE).
     * Restricción obligatoria, limitada a 20 caracteres.
     */
    @NotBlank(message = "El documento de identidad es obligatorio")
    @Column(nullable = false, length = 20)
    private String documentoIdentidad;

    /**
     * Relación o vínculo familiar/social con el titular de la reserva.
     * Refactorizado a Enum para garantizar la integridad referencial y evitar
     * inconsistencias de texto libre.
     */
    @NotNull(message = "El parentesco es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Parentesco parentesco;

    /**
     * Referencia a la reserva principal a la cual está vinculado este acompañante.
     * Relación de cardinalidad muchos a uno; una reserva puede contener múltiples acompañantes.
     */
    @ManyToOne
    @JoinColumn(name = "id_reserva", nullable = false)
    private Reserva reserva;
}