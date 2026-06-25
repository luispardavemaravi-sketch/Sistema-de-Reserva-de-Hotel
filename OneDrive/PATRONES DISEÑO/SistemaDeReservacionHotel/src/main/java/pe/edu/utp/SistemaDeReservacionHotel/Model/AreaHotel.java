package pe.edu.utp.sistemadereservacionhotel.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "area_hotel")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AreaHotel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAreaHotel;

    private String nombreArea;
    private String ubicacion;
}
