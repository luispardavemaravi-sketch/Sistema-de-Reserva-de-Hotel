package pe.edu.utp.sistemadereservacionhotel.model.servicio;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

/**
 * Entidad paramétrica que define las zonas o departamentos físicos del hotel.
 * Permite segmentar operativamente la ubicación de los servicios.
 */
@Entity
@Table(name = "area_hotel")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AreaHotel implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Identificador único del área a nivel de base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAreaHotel;

    /**
     * Denominación comercial u operativa del área (ej. "Spa", "Restaurante Principal").
     * Restricción de unicidad estricta para evitar zonas duplicadas.
     */
    @NotBlank(message = "El nombre del área es obligatorio")
    @Column(unique = true, nullable = false, length = 100)
    private String nombreArea;

    /**
     * Referencia espacial o ubicación física del área dentro del complejo.
     */
    @NotBlank(message = "La ubicación del área es obligatoria")
    @Column(nullable = false, length = 100)
    private String ubicacion;
}