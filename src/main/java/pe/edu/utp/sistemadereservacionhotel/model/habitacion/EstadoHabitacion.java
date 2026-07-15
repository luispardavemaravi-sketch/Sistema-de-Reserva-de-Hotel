package pe.edu.utp.sistemadereservacionhotel.model.habitacion;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Entidad de catálogo que define los posibles estados físicos y operativos de una habitación.
 * Actúa como un semáforo para el sistema de reservas, determinando qué habitaciones
 * están disponibles para ser asignadas a los clientes.
 */
@Entity
@Table(name = "estado_habitacion")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EstadoHabitacion implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Identificador único del estado a nivel de base de datos.
     * Clave primaria gestionada automáticamente por la estrategia de identidad del motor relacional.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEstado;

    /**
     * Denominación descriptiva del estado (ej. "Disponible", "Mantenimiento", "Ocupada").
     * Restricción de unicidad estricta para mantener la consistencia del catálogo.
     * Longitud máxima limitada a 40 caracteres a nivel de esquema.
     */
    @NotBlank(message = "El nombre del estado es obligatorio")
    @Column(unique = true, nullable = false, length = 40)
    private String nombreEstado;

    /**
     * Flag lógico que autoriza o bloquea la creación de reservas sobre una habitación en este estado.
     * Valor true: la habitación está libre y operativa.
     * Valor false: la habitación está bloqueada por ocupación, limpieza o problemas técnicos.
     */
    @NotNull(message = "Debe indicar si el estado es reservable")
    @Column(nullable = false)
    private Boolean esReservable;
}