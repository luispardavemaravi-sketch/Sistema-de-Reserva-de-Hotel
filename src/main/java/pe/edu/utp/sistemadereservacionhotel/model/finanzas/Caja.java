package pe.edu.utp.sistemadereservacionhotel.model.finanzas;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import pe.edu.utp.sistemadereservacionhotel.model.personal.Empleado;

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

    @NotNull(message = "La fecha es obligatoria")
    @Column(nullable = false)
    private LocalDate fecha;

    @PositiveOrZero(message = "El monto de apertura no puede ser negativo")
    @Column(nullable = false)
    private Double montoApertura;

    @PositiveOrZero(message = "El monto de cierre no puede ser negativo")
    private Double montoCierre;

    @NotNull(message = "Debe indicar si la caja está abierta")
    @Column(nullable = false)
    private Boolean estaAbierta = true;

    @ManyToOne
    @JoinColumn(name = "id_empleado", nullable = false)
    private Empleado empleado;
}