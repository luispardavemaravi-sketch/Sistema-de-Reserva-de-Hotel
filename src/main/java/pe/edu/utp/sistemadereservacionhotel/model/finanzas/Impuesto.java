package pe.edu.utp.sistemadereservacionhotel.model.finanzas;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idImpuesto;

    @NotBlank(message = "El nombre del impuesto es obligatorio")
    @Column(unique = true, nullable = false, length = 50)
    private String nombreImpuesto;

    /**
     * Tasa porcentual a aplicar sobre la base imponible.
     * CORRECCIÓN CRÍTICA: Refactorizado de Double a BigDecimal para evitar
     * pérdida de precisión en coma flotante y resolver el conflicto de tipos con el DTO.
     */
    @NotNull(message = "El porcentaje es obligatorio")
    @PositiveOrZero(message = "El porcentaje no puede ser negativo")
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal porcentaje;
}