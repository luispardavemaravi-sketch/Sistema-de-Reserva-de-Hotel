package pe.edu.utp.sistemadereservacionhotel.model.habitacion;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * Entidad de dominio que representa la agrupación arquitectónica o espacial de las habitaciones.
 * Facilita la gestión del inventario físico y la segmentación operativa del hotel.
 */
@Entity
@Table(name = "piso")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Piso implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Identificador único del piso a nivel de base de datos.
     * Clave primaria delegada a la estrategia de identidad del motor relacional.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPiso;

    /**
     * Nivel numérico del piso dentro de la infraestructura del hotel.
     * Mantiene una restricción de unicidad estricta para evitar inconsistencias espaciales.
     */
    @NotNull(message = "El número de piso es obligatorio")
    @Column(unique = true, nullable = false)
    private Integer numeroPiso;

    /**
     * Identificador del bloque, ala o zona específica en la que se ubica el piso (ej. "Ala Norte", "Torre A").
     * Limitado a 50 caracteres a nivel de esquema DDL.
     */
    @Column(length = 50, nullable = false)
    private String sector;

    /**
     * Colección de habitaciones asignadas físicamente a este piso.
     * Relación de cardinalidad uno a muchos.
     * Nota técnica: Se ha purgado el CascadeType.ALL para prevenir la eliminación destructiva
     * del inventario. La propagación se limita a persistencia y actualización (PERSIST, MERGE).
     */
    @OneToMany(mappedBy = "piso", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Habitacion> habitaciones;
}