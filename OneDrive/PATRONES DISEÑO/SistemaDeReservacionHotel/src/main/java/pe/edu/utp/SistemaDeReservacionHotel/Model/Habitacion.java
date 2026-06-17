package pe.edu.utp.SistemaDeReservacionHotel.Model;

import jakarta.persistence.*;
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

    private String numeroHabitacion;
    private String descripcion;
    private Double precioActual;
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
