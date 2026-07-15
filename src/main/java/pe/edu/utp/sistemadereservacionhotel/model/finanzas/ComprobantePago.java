package pe.edu.utp.sistemadereservacionhotel.model.finanzas;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.Reserva;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


/**
 * Entidad que representa el comprobante de pago físico o electrónico de una reserva.
 * Centraliza los datos de cabecera de facturación y mantiene la integridad referencial
 * con la reserva que lo origina y los detalles cobrados.
 */
@Entity
@Table(name = "comprobante_pago")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComprobantePago implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Identificador único del comprobante a nivel de base de datos.
     * Clave primaria autogenerada.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idComprobante;

    /**
     * Código alfanumérico legal o interno del comprobante (ej. F001-000456).
     * Restricción de unicidad estricta para evitar colisiones en la facturación.
     */
    @NotBlank(message = "El número de serie es obligatorio")
    @Column(unique = true, nullable = false, length = 20)
    private String numeroSerie;

    /**
     * Marca de tiempo exacta en la que se generó la transacción.
     * Bloqueada para actualizaciones (updatable = false) para asegurar la trazabilidad.
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaEmision;

    /**
     * Sumatoria final del costo de los servicios facturados.
     * Restringido a valores positivos o cero por lógica financiera.
     */
    @NotNull(message = "El monto total es obligatorio")
    @PositiveOrZero(message = "El monto total no puede ser negativo")
    @Column(nullable = false)
    private Double montoTotal;

    /**
     * Vínculo directo con la reserva pagada.
     * Relación uno a uno; un comprobante liquida exactamente una reserva.
     */
    @OneToOne
    @JoinColumn(name = "id_reserva", nullable = false)
    private Reserva reserva;

    /**
     * Colección de los ítems o servicios desglosados en este comprobante.
     * Relación de composición: si se elimina el comprobante, se eliminan sus detalles en cascada.
     */
    @OneToMany(mappedBy = "comprobantePago", cascade = CascadeType.ALL)
    private List<DetalleComprobante> detalles;
}