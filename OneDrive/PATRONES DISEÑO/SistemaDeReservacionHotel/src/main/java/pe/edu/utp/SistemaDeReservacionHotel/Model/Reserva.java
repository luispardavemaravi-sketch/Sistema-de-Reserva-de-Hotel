package pe.edu.utp.SistemaDeReservacionHotel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "reserva")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reserva implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReserva;

    @NotBlank
    @Column(unique = true, nullable = false, length = 100)
    private String codigoReserva;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaReserva;

    @NotBlank(message = "La fecha de entrada planificada es obligatoria")
    @Column(nullable = false)
    private LocalDate fechaEntradaPlanificada;

    @PositiveOrZero(message = "El monto total no puede ser negativo")
    @Column(nullable = false )
    private Double montoTotalEstimado;

    // Relación N a 1: Muchas reservas pertenecen a un Huésped

    @ManyToOne
    @JoinColumn(name = "id_huesped", nullable = false)
    private Huesped huesped;

    // Relación N a 1: Muchas reservas comparten un mismo Estado
    @ManyToOne
    @JoinColumn(name = "id_estado_reserva", nullable = false)
    private EstadoReserva estadoReserva;

    // Relación 1 a 1: Una reserva tiene un único Check-In
    @OneToOne(mappedBy = "reserva", cascade = CascadeType.ALL)
    private CheckIn checkIn;

    // Relación 1 a 1: Una reserva tiene un único Check-Out
    @OneToOne(mappedBy = "reserva", cascade = CascadeType.ALL)
    private CheckOut checkOut;

    // Relación 1 a N: Una reserva puede tener muchos acompañantes
    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL)
    private List<Acompanante> acompanantes;
}
