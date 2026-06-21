package pe.edu.utp.SistemaDeReservacionHotel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "El nombre del área es obligatorio")
    @Column(nullable = false, length = 40)
    private String nombreArea;

    @NotBlank(message = "La ubicación del área es obligatoria")
    @Column(length = 40)
    private String ubicacion;
}
