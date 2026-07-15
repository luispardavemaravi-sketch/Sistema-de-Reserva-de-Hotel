package pe.edu.utp.sistemadereservacionhotel.model.finanzas;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.io.Serializable;

/**
 * Entidad de catálogo que define los impuestos aplicables en el sistema de facturación.
 * Permite la gestión dinámica de las tasas impositivas (ej. IVA) para su cálculo en
 * los comprobantes de pago.
 */
@Entity
@Table(name = "impuesto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Impuesto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Identificador único del registro de impuesto.
     * Clave primaria autogenerada por el motor de base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idImpuesto;

    /**
     * Denominación oficial del tributo (ej. "IVA 21%", "IGV 18%").
     * Restricción de unicidad estricta para evitar duplicidades en el esquema fiscal.
     * Longitud máxima restringida a 50 caracteres a nivel de base de datos.
     */
    @NotBlank(message = "El nombre del impuesto es obligatorio")
    @Column(unique = true, nullable = false, length = 50)
    private String nombreImpuesto;

    /**
     * Tasa porcentual a aplicar sobre la base imponible.
     * Restringido a valores positivos o cero para mantener la consistencia matemática
     * en los cálculos financieros.
     */
    @NotNull(message = "El porcentaje es obligatorio")
    @PositiveOrZero(message = "El porcentaje no puede ser negativo")
    @Column(nullable = false)
    private Double porcentaje;
}