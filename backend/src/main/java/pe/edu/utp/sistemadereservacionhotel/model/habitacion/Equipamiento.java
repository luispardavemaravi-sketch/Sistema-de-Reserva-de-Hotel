package pe.edu.utp.sistemadereservacionhotel.model.habitacion;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Entidad de catálogo que define los recursos, comodidades o mobiliario adicional
 * disponible para las habitaciones. Gestiona el inventario de servicios físicos
 * y determina su impacto financiero sobre el costo de la reserva.
 */
@Entity
@Table(name = "equipamiento")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Equipamiento implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Identificador único del equipamiento a nivel de base de datos.
     * Clave primaria gestionada automáticamente por el motor de persistencia.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEquipamiento;

    /**
     * Denominación descriptiva del recurso (ej. "Cama adicional", "Minibar").
     * Posee una restricción de unicidad estricta para evitar registros duplicados
     * en el catálogo. Limitado a 100 caracteres en el esquema relacional.
     */
    @NotBlank(message = "El nombre del equipamiento es obligatorio")
    @Column(unique = true, nullable = false, length = 100)
    private String nombre;

    /**
     * Tarifa extra aplicable por el uso o solicitud de este equipamiento.
     * Inicializado en 0.0 a nivel de instancia para evitar valores nulos.
     * Restringido a valores positivos o cero para prevenir inconsistencias en la facturación.
     */
    @NotNull(message = "El costo adicional no puede ser nulo")
    @PositiveOrZero(message = "El costo adicional no puede ser negativo")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal costoAdicional;

}