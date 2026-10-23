package pe.edu.utp.sistemadereservacionhotel.model.habitacion;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * Entidad central del dominio que representa una habitación física dentro del hotel.
 * Consolida las características base, el estado operativo, el tipo, la ubicación
 * y el inventario de equipamiento asociado, sirviendo como pivote transaccional
 * para el módulo de reservas.
 */
@Entity
@Table(name = "habitacion")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Habitacion implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Identificador único de la habitación a nivel de base de datos.
     * Clave primaria gestionada automáticamente por la estrategia de identidad.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHabitacion;

    /**
     * Identificador alfanumérico visible para el usuario (ej. "101A").
     * Mantiene una restricción de unicidad estricta e inmutable para evitar
     * colisiones en la asignación física y operativa.
     */
    @NotBlank(message = "El número de habitación es obligatorio")
    @Column(length = 100, nullable = false, unique = true)
    private String numeroHabitacion;

    /**
     * Notas operativas o especificaciones concretas de la habitación.
     * Limitado a 255 caracteres en persistencia.
     */
    @Column(length = 255)
    private String descripcion;

    /**
     * Tarifa base vigente aplicada en el momento actual para esta habitación.
     * Restringido exclusivamente a valores positivos o cero.
     * Nota técnica: Refactorizado a BigDecimal para garantizar precisión transaccional y
     * evitar las vulnerabilidades de redondeo de los tipos de coma flotante.
     */
    @NotNull(message = "El precio actual es obligatorio")
    @PositiveOrZero(message = "El precio actual debe ser un valor positivo o cero")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioActual;

    /**
     * Bandera para el control de borrado lógico (Soft Delete).
     * Determina si la habitación está visible e interacciona en el sistema
     * sin necesidad de destruir su historial relacional.
     */
    @Column(nullable = false)
    private boolean estadoActivo;

    /**
     * Referencia a la entidad arquitectónica (Piso) donde se localiza.
     * Relación de cardinalidad muchos a uno. Forzada a carga diferida (LAZY)
     * para evitar el problema de rendimiento N+1.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_piso")
    private Piso piso;

    /**
     * Categorización base de la habitación (ej. Suite, Doble) que define
     * parámetros comerciales globales. Relación muchos a uno. Carga diferida (LAZY).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo")
    private TipoHabitacion tipoHabitacion;

    /**
     * Semáforo operativo de la habitación (ej. Disponible, Mantenimiento, Limpieza).
     * Determina su disponibilidad en la máquina de estados del sistema. Carga diferida (LAZY).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estado_habitacion")
    private EstadoHabitacion estadoHabitacion;

    /**
     * Registro histórico auditable de las variaciones de tarifa que ha sufrido
     * esta habitación a lo largo del tiempo.
     * Relación uno a muchos, con operaciones en cascada.
     */
    @OneToMany(mappedBy = "habitacion", cascade = CascadeType.ALL)
    private List<PrecioHabitacion> historialPrecios;

    /**
     * Colección de comodidades y mobiliario extra asignado físicamente a la habitación.
     * Relación muchos a muchos, materializada a través de la tabla intermedia.
     * Nota técnica: Refactorizado de List a Set para evitar ineficiencias graves
     * del ORM al eliminar o actualizar registros en la tabla pivote.
     */
    @ManyToMany
    @JoinTable(
            name = "habitacion_equipamiento",
            joinColumns = @JoinColumn(name = "id_habitacion"),
            inverseJoinColumns = @JoinColumn(name = "id_equipamiento")
    )
    private Set<Equipamiento> equipamientos;
}