package pe.edu.utp.sistemadereservacionhotel.model.personal;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.utp.sistemadereservacionhotel.config.Auditor;

import java.io.Serializable;

/**
 * Entidad de dominio que representa al personal contratado por el hotel.
 * Gestiona los datos personales, de contacto y el rol organizativo del empleado,
 * extendiendo de la clase Auditor para mantener la trazabilidad de creación y modificación.
 */
@Entity
@Table(name = "empleado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Empleado extends Auditor<String> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Identificador único del empleado a nivel de base de datos.
     * Clave primaria gestionada automáticamente por la estrategia de identidad.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEmpleado;

    /**
     * Nombre(s) de pila del empleado.
     * Restricción obligatoria, limitada a 100 caracteres en la persistencia.
     */
    @NotBlank(message = "El nombre no puede estar vacío.")
    @Column(nullable = false, length = 100)
    private String nombre;

    /**
     * Apellidos del empleado.
     * Restricción obligatoria, limitada a 100 caracteres.
     */
    @NotBlank(message = "El apellido no puede estar vacío.")
    @Column(nullable = false, length = 100)
    private String apellido;

    /**
     * Dirección de correo electrónico corporativo o personal del empleado.
     * Restricción de unicidad estricta.
     * Nota técnica: La validación a nivel de clase es deficiente. Falta la anotación
     *
     * @NotBlank, lo que permite que un valor nulo pase la validación del framework
     * y provoque una excepción de SQL al chocar con el constraint nullable = false.
     */

    @NotBlank(message = "El correo electrónico es obligatorio.")
    @Email(message = "El correo electrónico debe ser válido.")
    @Column(unique = true, nullable = false, length = 150)
    private String email;

    /**
     * Número telefónico de contacto del empleado.
     * Restricción obligatoria, limitada a 20 caracteres en esquema DDL.
     */
    @NotBlank(message = "El teléfono no puede estar vacío.")
    @Column(nullable = false, length = 20)
    private String telefono;

    /**
     * Puesto o función jerárquica que ocupa el empleado en el hotel.
     * Nota técnica de diseño: El uso de texto libre (String) para definir roles organizativos
     * es un antipatrón de diseño relacional. Debería refactorizarse hacia una relación
     *
     * @ManyToOne o una enumeración (Enum) para garantizar la consistencia de los datos.
     */
    @NotNull(message = "El cargo es obligatorio.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private CargoEmpleado cargo;

    /**
     * Área de dominio técnico u operativo del empleado.
     * Nota técnica de diseño: Al igual que el atributo cargo, el uso de texto libre
     * vulnera la normalización de la base de datos.
     */
    @NotNull(message = "La especialidad es obligatoria.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private EspecialidadEmpleado especialidad;

    /**
     * Domicilio físico de residencia del empleado.
     * Restricción obligatoria, limitada a 255 caracteres para abarcar direcciones complejas.
     * Consideración legal: Dato clasificado como PII (Información de Identificación Personal),
     * sujeto a normativas estrictas de protección de datos (ej. RGPD).
     */
    @NotBlank(message = "La dirección no puede estar vacía.")
    @Column(nullable = false, length = 255)
    private String direccion;

    /**
     * Ciudad de residencia actual del empleado.
     * Restricción obligatoria, limitada a 100 caracteres.
     */
    @NotBlank(message = "La ciudad no puede estar vacía.")
    @Column(nullable = false, length = 100)
    private String ciudad;


    /**
     * Bandera de borrado lógico para cumplir con auditorías y normativas de datos (RGPD).
     */
    @NotNull(message = "El estado activo es obligatorio")
    @Column(nullable = false)
    private Boolean estadoActivo = true;

    /**
     * Relación con la malla horaria. Un empleado debe tener un turno asignado.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_turno", nullable = false)
    private Turno turno;
}