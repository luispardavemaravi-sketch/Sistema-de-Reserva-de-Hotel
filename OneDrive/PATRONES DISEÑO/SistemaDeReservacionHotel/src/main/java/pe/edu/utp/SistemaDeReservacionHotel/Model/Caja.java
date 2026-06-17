package pe.edu.utp.SistemaDeReservacionHotel.Model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "caja")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Caja implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCaja;

    private LocalDate fecha;
    private Double montoApertura;
    private Double montoCierre;
    private Boolean estaAbierta;

    @ManyToOne
    @JoinColumn(name = "id_empleado")
    private Empleado empleado;
}