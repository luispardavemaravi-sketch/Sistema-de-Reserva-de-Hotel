package pe.edu.utp.sistemadereservacionhotel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "servicio_adicional")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServicioAdicional implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idServicio;

    private String nombre;
    private String descripcion;
    private Double precioUnitario;
    private Boolean estaDisponible;
}