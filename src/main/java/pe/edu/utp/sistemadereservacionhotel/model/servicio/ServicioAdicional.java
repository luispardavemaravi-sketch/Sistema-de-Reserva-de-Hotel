package pe.edu.utp.sistemadereservacionhotel.model.servicio;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Entidad que define el inventario físico o lógico de servicios extra.
 * Delega la responsabilidad financiera exclusivamente a la entidad CatalogoServicio.
 */
@Entity
@Table(name = "servicio_adicional")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServicioAdicional implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Identificador único del servicio a nivel de base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idServicio;

    /**
     * Denominación comercial del servicio ofrecido.
     */
    @NotBlank(message = "El nombre del servicio es obligatorio")
    @Column(unique = true, nullable = false, length = 100)
    private String nombre;

    /**
     * Detalles específicos sobre las condiciones o características del servicio.
     */
    @Column(length = 255)
    private String descripcion;

    /**
     * Flag lógico que determina si el servicio puede ser ofrecido actualmente.
     */
    @NotNull(message = "Debe indicar si el servicio está disponible")
    @Column(nullable = false)
    private Boolean estaDisponible = true;
}