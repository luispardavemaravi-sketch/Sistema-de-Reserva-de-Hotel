package pe.edu.utp.SistemaDeReservacionHotel.Model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "impuesto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Impuesto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idImpuesto;

    private String nombreImpuesto;
    private Double porcentaje; // Ej: 0.18 para IGV
}