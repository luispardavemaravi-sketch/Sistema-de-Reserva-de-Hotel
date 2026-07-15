package pe.edu.utp.sistemadereservacionhotel.model.finanzas;

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

/**
 * Entidad que representa cada ítem o línea individual dentro de un comprobante de pago.
 * Mantiene el registro desagregado de los servicios, productos o habitaciones cobradas,
 * vinculando cantidades y costos unitarios con el cálculo de su subtotal.
 */
@Entity
@Table(name = "detalle_comprobante")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleComprobante implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Identificador único de la línea de detalle a nivel de base de datos.
     * Clave primaria autogenerada.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalle;

    /**
     * Número de unidades del servicio o producto facturado en esta línea.
     * Restringido estrictamente a enteros positivos mayores a cero.
     */
    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a cero")
    @Column(nullable = false)
    private Integer cantidad;

    /**
     * Especificación o concepto de la línea facturada (ej. "Noche de habitación estándar").
     * Campo obligatorio, limitado a 255 caracteres en persistencia.
     */
    @NotBlank(message = "La descripción es obligatoria")
    @Column(nullable = false, length = 255)
    private String descripcion;

    /**
     * Costo individual del servicio o producto antes de calcular cantidades o impuestos.
     * Valor obligatorio y estrictamente mayor a cero.
     */
    @NotNull(message = "El precio unitario es obligatorio")
    @Positive(message = "El precio unitario debe ser mayor a cero")
    @Column(nullable = false)
    private Double precioUnitario;

    /**
     * Importe monetario total de la línea (resultado de la operación cantidad * precioUnitario).
     * No permite valores negativos y se almacena en base de datos de forma persistente.
     */
    @PositiveOrZero(message = "El subtotal no puede ser negativo")
    @Column(nullable = false)
    private Double subtotalLinea;

    /**
     * Referencia al comprobante de pago contenedor al que pertenece este ítem.
     * Relación de cardinalidad muchos a uno con la entidad ComprobantePago.
     */
    @ManyToOne
    @JoinColumn(name = "id_comprobante", nullable = false)
    private ComprobantePago comprobantePago;
}