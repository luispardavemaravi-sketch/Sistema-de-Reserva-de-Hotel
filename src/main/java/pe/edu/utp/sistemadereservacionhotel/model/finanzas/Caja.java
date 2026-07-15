package pe.edu.utp.sistemadereservacionhotel.model.finanzas;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import pe.edu.utp.sistemadereservacionhotel.model.personal.Empleado;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Autor:Luisangel Ronal Pardave Maravi
 * <p>
 * Entidad que representa el control de caja en el sistema de reservas del hotel.
 * Gestiona el ciclo de vida diario de los ingresos y egresos físicos, el estado
 * de apertura/cierre y la asignación a un empleado específico.
 */
@Entity
@Table(name = "caja")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Caja implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * Identificador único de la sesión de caja.
     * Generado automáticamente por la base de datos (clave primaria).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCaja;

    /**
     * Fecha operativa correspondiente al turno de la caja.
     * Campo obligatorio para auditoría y arqueo diario.
     */
    @NotNull(message = "La fecha es obligatoria")
    @Column(nullable = false)
    private LocalDate fecha;

    /**
     * Saldo inicial con el que el empleado inicia su turno.
     * Valor requerido, no puede ser negativo.
     */
    @PositiveOrZero(message = "El monto de apertura no puede ser negativo")
    @Column(nullable = false)
    private Double montoApertura;

    /**
     * Saldo final contabilizado al terminar el turno.
     * Permitido como nulo mientras el atributo estaAbierta sea verdadero.
     */
    @PositiveOrZero(message = "El monto de cierre no puede ser negativo")
    private Double montoCierre;

    /**
     * Indicador del estado transaccional de la caja.
     * Valor true: habilitada para procesar pagos.
     * Valor false: turno cerrado, bloqueada para nuevas operaciones.
     */
    @NotNull(message = "Debe indicar si la caja está abierta")
    @Column(nullable = false)
    private Boolean estaAbierta = true;

    /**
     * Referencia al empleado responsable de los fondos durante la sesión de caja.
     * Relación de cardinalidad muchos a uno con la entidad Empleado.
     */
    @ManyToOne
    @JoinColumn(name = "id_empleado", nullable = false)
    private Empleado empleado;
}