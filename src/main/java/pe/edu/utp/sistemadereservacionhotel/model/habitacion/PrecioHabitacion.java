package pe.edu.utp.sistemadereservacionhotel.model.habitacion;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidad que gestiona la temporalidad y el histórico de tarifas de una habitación.
 * Permite implementar modelos de precios dinámicos o estacionales, definiendo
 * la vigencia de un costo específico en un rango de fechas determinado.
 */
@Entity
@Table(name = "precio_habitacion")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PrecioHabitacion implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Identificador único del registro de tarifa a nivel de base de datos.
     * Clave primaria gestionada automáticamente por la estrategia de identidad.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPrecio;

    /**
     * Valor monetario asignado a la habitación durante el periodo de vigencia.
     * Restringido estrictamente a valores positivos mayores a cero.
     * Nota técnica: Para entornos financieros reales, se exige refactorizar a BigDecimal
     * para evitar pérdida de precisión en operaciones de coma flotante.
     */
    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor a cero")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    /**
     * Límite inferior del rango temporal de vigencia de la tarifa.
     * Campo obligatorio para la evaluación de precios activos en el motor de reservas.
     */
    @NotNull(message = "La fecha de inicio es obligatoria")
    @Column(nullable = false)
    private LocalDate fechaInicio;

    /**
     * Límite superior del rango temporal de caducidad de la tarifa.
     * Campo obligatorio. El sistema asume que la tarifa caduca superada esta fecha.
     */
    @NotNull(message = "La fecha fin es obligatoria")
    @Column(nullable = false)
    private LocalDate fechaFin;

    /**
     * Referencia a la habitación sobre la cual aplica esta regla de precio.
     * Relación de cardinalidad muchos a uno.
     * Nota técnica: Se aplica FetchType.LAZY obligatoriamente para bloquear la
     * carga temprana y evitar cuellos de botella por consultas N+1.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_habitacion", nullable = false)
    private Habitacion habitacion;
}