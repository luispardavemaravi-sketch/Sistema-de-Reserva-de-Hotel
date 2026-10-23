package pe.edu.utp.sistemadereservacionhotel.model.finanzas;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Entidad de catálogo que define las formas de pago aceptadas en el sistema.
 * Centraliza los métodos disponibles para liquidar transacciones y diferencia
 * su naturaleza (física o digital) para el enrutamiento del procesamiento.
 */
@Entity
@Table(name = "metodo_pago")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MetodoPago implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Identificador único del método de pago a nivel de base de datos.
     * Clave primaria gestionada automáticamente por el motor de persistencia.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMetodo;

    /**
     * Denominación oficial del medio de pago (ej. "Efectivo", "Tarjeta de Crédito", "Transferencia").
     * Posee una restricción de unicidad para evitar registros duplicados en el catálogo.
     * Limitado a 50 caracteres a nivel de esquema.
     */
    @NotBlank(message = "El nombre del método es obligatorio")
    @Column(unique = true, nullable = false, length = 50)
    private String nombreMetodo;

    /**
     * Flag que determina la naturaleza operativa del método de pago.
     * Valor true: requiere procesamiento electrónico o integración con pasarelas.
     * Valor false: transacción física o manual (caja).
     */
    @NotNull(message = "Debe indicar si es digital")
    @Column(nullable = false)
    private Boolean esDigital;
}