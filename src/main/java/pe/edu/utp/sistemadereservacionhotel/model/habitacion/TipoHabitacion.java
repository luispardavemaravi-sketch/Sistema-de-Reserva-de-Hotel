package pe.edu.utp.sistemadereservacionhotel.model.habitacion;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Entidad paramétrica que define las categorías globales de las habitaciones del hotel.
 * Actúa como un catálogo base que estandariza las capacidades máximas y establece
 * los costos referenciales de partida para el motor de precios.
 */
@Entity
@Table(name = "tipo_habitacion")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TipoHabitacion implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Identificador único de la categoría de habitación a nivel de base de datos.
     * Clave primaria delegada a la estrategia de autogeneración del motor relacional.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTipo;

    /**
     * Denominación comercial de la categoría (ej. "Suite Presidencial", "Doble Estándar").
     * Mantiene una restricción de unicidad estricta en el esquema para evitar
     * duplicidad de inventario. Limitado a 100 caracteres.
     */
    @NotBlank(message = "El nombre no debe estar vacio.")
    @Column(unique = true, nullable = false, length = 100)
    private String nombre;

    /**
     * Límite de ocupación física permitida para las habitaciones de este tipo.
     * Restringido estrictamente a números enteros positivos, bloqueando lógicamente
     * capacidades nulas o negativas.
     */
    @NotNull(message = "La capacidad máxima es obligatoria.")
    @Column(nullable = false)
    @Positive(message = "La capacidad máxima debe ser un valor positivo.")
    private Integer capacidadMaxima;

    /**
     * Tarifa monetaria por defecto asociada a la categoría.
     * Restringida matemáticamente a valores positivos o cero.
     * Nota técnica: Contradicción en la capa de validación; el mensaje asume control de nulidad,
     * pero la anotación carece de la restricción @NotNull. Además, el uso de Double para
     * persistencia financiera viola los estándares de precisión (requiere refactorización a BigDecimal).
     */
    @PositiveOrZero(message = "El precio base no puede estar vacio.")
    @Column(nullable = false)
    private BigDecimal precioBase;
}