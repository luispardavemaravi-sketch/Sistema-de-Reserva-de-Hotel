package pe.edu.utp.sistemadereservacionhotel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "detalle_comprobante")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleComprobante implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalle;

    private Integer cantidad;
    private String descripcion;
    private Double precioUnitario;
    private Double subtotalLinea;

    @ManyToOne
    @JoinColumn(name = "id_comprobante")
    private ComprobantePago comprobantePago;
}