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
 * Entidad paramétrica que define la máquina de estados por la que transita una reserva
 * (ej. Pendiente, Confirmada, Cancelada, Completada).
 */
@Entity
@Table(name = "estado_reserva")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstadoReserva implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Identificador único del estado a nivel de base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEstado;

    /**
     * Denominación oficial del estado.
     * Restricción de unicidad estricta, limitada a 50 caracteres.
     */
    @NotBlank(message = "El nombre del estado es obligatorio")
    @Column(unique = true, nullable = false, length = 50)
    private String nombreEstado;

    /**
     * Flag de control lógico que determina si una reserva en este estado puede sufrir
     * alteraciones en su configuración (fechas, habitaciones, acompañantes).
     */
    @NotNull(message = "Debe indicar si el estado es modificable")
    @Column(nullable = false)
    private Boolean esModificable;
}