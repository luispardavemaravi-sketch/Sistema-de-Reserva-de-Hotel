package pe.edu.utp.sistemadereservacionhotel.model.servicio;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Entidad que gestiona la categorización y las tarifas dinámicas o vigentes
 * de los servicios. Centraliza la lógica de precios del módulo.
 */
@Entity
@Table(name = "catalogo_servicio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CatalogoServicio implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Identificador único del registro en el catálogo.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCatalogo;

    /**
     * Tarifa monetaria actual aplicada al servicio.
     */
    @NotNull(message = "El precio vigente es obligatorio")
    @PositiveOrZero(message = "El precio no puede ser negativo")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioVigente;

    /**
     * Clasificación agrupada del servicio mapeada a un dominio cerrado.
     */
    @NotNull(message = "La categoría es obligatoria")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private CategoriaServicio categoria;

    /**
     * Referencia al servicio base que está siendo catalogado.
     * Corregido: Se aplicó FetchType.LAZY. Se eliminó 'unique = true' para
     * permitir a un mismo servicio poseer un registro de tarifas por temporadas.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_servicio", nullable = false)
    private ServicioAdicional servicioAdicional;
}