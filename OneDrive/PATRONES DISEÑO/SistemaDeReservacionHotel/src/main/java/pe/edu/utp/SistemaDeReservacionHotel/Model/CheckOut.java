package pe.edu.utp.SistemaDeReservacionHotel.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "check_out")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckOut implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCheckOut;

    private LocalDateTime fechaHoraRealSalida;
    private Double multaPorRetraso;

    // Relación 1 a 1 inversa hacia Reserva
    @OneToOne
    @JoinColumn(name = "id_reserva", unique = true, nullable = false)
    private Reserva reserva;
}