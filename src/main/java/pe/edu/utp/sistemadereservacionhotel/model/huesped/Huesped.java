package pe.edu.utp.sistemadereservacionhotel.model.huesped;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.Reserva;

import java.io.Serializable;
import java.util.List;

/**
 * Entidad maestra que representa al cliente o huésped dentro del sistema.
 * Centraliza la información de identidad y contacto, sirviendo como eje
 * para el registro de operaciones y reservas a lo largo del tiempo.
 */
@Entity
@Table(name = "huesped")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Huesped implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Identificador único del huésped a nivel de base de datos.
     * Clave primaria gestionada automáticamente por la estrategia de identidad.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHuesped;

    /**
     * Nombre(s) de pila del cliente.
     * Restricción obligatoria, limitada a 100 caracteres en la persistencia.
     */
    @NotBlank(message = "El nombre no puede estar vacío.")
    @Column(nullable = false, length = 100)
    private String nombre;

    /**
     * Apellidos del cliente.
     * Restricción obligatoria, limitada a 100 caracteres en la persistencia.
     */
    @NotBlank(message = "El apellido no puede estar vacío.")
    @Column(nullable = false, length = 100)
    private String apellidos;

    /**
     * Número de documento oficial de identidad (DNI, Pasaporte, NIE).
     * Mantiene una restricción de unicidad estricta para prevenir la duplicidad
     * de perfiles de usuario. Limitado a 20 caracteres.
     */
    @NotBlank(message = "El documento de identidad es obligatorio")
    @Column(unique = true, nullable = false, length = 20)
    private String documentoIdentidad;

    /**
     * Dirección de correo electrónico de contacto.
     * Validado por formato estándar de email. Restricción de unicidad estricta
     * para su posible uso como credencial de acceso o notificaciones.
     */
    @NotBlank(message = "El correo electrónico no puede estar vacío.")
    @Email(message = "El correo electrónico debe ser válido.")
    @Column(unique = true, nullable = false, length = 150)
    private String email;

    /**
     * Número telefónico de contacto directo.
     * Campo opcional a nivel de aplicación, limitado a 20 caracteres en esquema DDL.
     */
    @Column(length = 20)
    private String telefono;

    /**
     * Flag para implementar borrado lógico (Soft Delete).
     * Obligatorio para cumplir con normativas de retención de datos donde
     * no se puede realizar un borrado físico en base de datos.
     */
    @Column(nullable = false)
    private boolean estadoActivo = true;

    /**
     * Colección del registro histórico de reservas generadas por este huésped.
     * Relación de cardinalidad uno a muchos.
     * Nota técnica y de cumplimiento: La aplicación de CascadeType.ALL es una
     * vulnerabilidad arquitectónica y legal. La eliminación de la entidad maestra
     * destruirá las transacciones (Reservas), violando principios de auditoría
     * financiera y normativas de retención de datos.
     */
    @OneToMany(mappedBy = "huesped", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Reserva> historialReservas;
}