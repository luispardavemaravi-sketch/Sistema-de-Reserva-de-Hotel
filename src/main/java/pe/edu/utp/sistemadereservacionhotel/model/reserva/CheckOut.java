package pe.edu.utp.sistemadereservacionhotel.model.reserva;

import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "check_out")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class CheckOut implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCheckOut;

    @Column(updatable = false)  // se asigna automáticamente al crear
    private LocalDateTime fechaHoraRealSalida;

    @PositiveOrZero(message = "La multa no puede ser negativa")
    @Column(nullable = false)
    private Double multaPorRetraso = 0.0;  // valor por defecto 0

    @OneToOne
    @JoinColumn(name = "id_reserva", unique = true, nullable = false)
    private Reserva reserva;
}