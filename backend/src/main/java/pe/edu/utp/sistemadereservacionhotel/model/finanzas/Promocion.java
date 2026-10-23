package pe.edu.utp.sistemadereservacionhotel.model.finanzas;

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
import java.time.LocalDate;

/**
 * Entidad que define las promociones o cupones de descuento aplicables a las reservas.
 * Establece las reglas de reducción de precio, los códigos de acceso a las ofertas
 * y controla la vigencia temporal de las mismas.
 */
@Entity
@Table(name = "promocion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Promocion implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Identificador único de la promoción a nivel de base de datos.
     * Clave primaria delegada al motor de persistencia.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPromocion;

    /**
     * Código alfanumérico que el usuario o sistema utiliza para reclamar la oferta.
     * Restricción de unicidad obligatoria para evitar colisiones en la validación de cupones.
     * Longitud máxima limitada a 20 caracteres a nivel de esquema.
     */
    @NotBlank(message = "El código de cupón es obligatorio")
    @Column(unique = true, nullable = false, length = 20)
    private String codigoCupon;

    /**
     * Valor porcentual que se deducirá del monto total o subtotal.
     * Restringido exclusivamente a valores positivos o cero para garantizar
     * la integridad de las operaciones matemáticas en la facturación.
     */
    // EN TU ENTIDAD Promocion.java DEBES PONER ESTO:
    @NotNull(message = "El porcentaje de descuento es obligatorio")
    @PositiveOrZero(message = "El descuento no puede ser negativo")
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal porcentajeDescuento; // ESTO ERA UN DOUBLE. CÁMBIALO A BIGDECIMAL.

    /**
     * Fecha límite hasta la cual el cupón es válido y puede ser procesado.
     * Campo obligatorio, fundamental para la lógica de negocio que evalúa
     * la vigencia antes de aplicar cualquier deducción.
     */
    @NotNull(message = "La fecha de caducidad es obligatoria")
    @Column(nullable = false)
    private LocalDate fechaCaducidad;
}