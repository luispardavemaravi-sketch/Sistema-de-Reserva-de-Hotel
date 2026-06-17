package pe.edu.utp.SistemaDeReservacionHotel.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "check_in")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckIn implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCheckIn;

    private LocalDateTime fechaHoraRealEntrada;
    private String observaciones;

    // Relación 1 a 1 inversa hacia Reserva
    @OneToOne
    @JoinColumn(name = "id_reserva", unique = true, nullable = false)
    private Reserva reserva;
}