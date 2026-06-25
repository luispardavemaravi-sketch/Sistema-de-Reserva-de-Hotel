package pe.edu.utp.sistemadereservacionhotel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "habitacion")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Habitacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHabitacion;

    @NotBlank(message = "El número de habitación es obligatorio")
    @Column(length = 100, nullable = false, unique = true)
    private String numeroHabitacion;

    @Column(length = 255)
    private String descripcion;

    @NotNull(message = "El precio actual es obligatorio")
    @PositiveOrZero(message = "El precio actual debe ser un valor positivo o cero")
    @Column(length = 255, nullable = false)
    private Double precioActual;

    @Column(length = 255, nullable = false)
    private boolean estadoActivo;

    @ManyToOne
    @JoinColumn(name = "id_piso")
    private Piso piso;

    // Relación N a 1 (Muchas habitaciones tienen un Tipo)
    @ManyToOne
    @JoinColumn(name = "id_tipo")
    private TipoHabitacion tipoHabitacion;

    // Relación N a 1 (Muchas habitaciones tienen un Estado)
    @ManyToOne
    @JoinColumn(name = "id_estado_habitacion")
    private EstadoHabitacion estadoHabitacion;

    // Relación 1 a N (Una habitación tiene un historial de precios)
    @OneToMany(mappedBy = "habitacion", cascade = CascadeType.ALL)
    private List<PrecioHabitacion> historialPrecios;

    // Relación N a M (Muchos equipamientos en muchas habitaciones)
    @ManyToMany
    @JoinTable(
            name = "habitacion_equipamiento",
            joinColumns = @JoinColumn(name = "id_habitacion"),
            inverseJoinColumns = @JoinColumn(name = "id_equipamiento")
    )
    private List<Equipamiento> equipamientos;
}
