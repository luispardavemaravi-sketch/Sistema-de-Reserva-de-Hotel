package pe.edu.utp.sistemadereservacionhotel.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "catalogo_servicio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CatalogoServicio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCatalogo;

    private Double precioVigente;
    private String categoria;


}



