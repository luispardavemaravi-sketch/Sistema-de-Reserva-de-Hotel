package pe.edu.utp.SistemaDeReservacionHotel.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "comprobante_pago")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComprobantePago implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idComprobante;

    private String numeroSerie;
    private LocalDateTime fechaEmision;
    private Double montoTotal;

    @OneToOne
    @JoinColumn(name = "id_reserva")
    private Reserva reserva;

    @OneToMany(mappedBy = "comprobantePago", cascade = CascadeType.ALL)
    private List<DetalleComprobante> detalles;
}
