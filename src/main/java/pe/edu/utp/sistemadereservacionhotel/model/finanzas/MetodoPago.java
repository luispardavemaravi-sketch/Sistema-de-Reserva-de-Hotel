package pe.edu.utp.sistemadereservacionhotel.model.finanzas;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "metodo_pago")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MetodoPago implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMetodo;

    @NotBlank(message = "El nombre del método es obligatorio")
    @Column(unique = true, nullable = false, length = 50)
    private String nombreMetodo;

    @NotNull(message = "Debe indicar si es digital")
    @Column(nullable = false)
    private Boolean esDigital;
}