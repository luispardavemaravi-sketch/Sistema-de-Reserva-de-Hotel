package pe.edu.utp.SistemaDeReservacionHotel.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "consumo_servicio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsumoServicio implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idConsumo;

    private LocalDateTime fechaHora;
    private Integer cantidad;
    private Double subTotal;

    @ManyToOne
    @JoinColumn(name = "id_reserva")
    private Reserva reserva;

    @ManyToOne
    @JoinColumn(name = "id_servicio")
    private ServicioAdicional servicioAdicional;
}
