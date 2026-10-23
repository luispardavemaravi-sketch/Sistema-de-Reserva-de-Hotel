package pe.edu.utp.sistemadereservacionhotel.model.reserva;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import pe.edu.utp.sistemadereservacionhotel.config.Auditor;
import pe.edu.utp.sistemadereservacionhotel.model.habitacion.Habitacion;
import pe.edu.utp.sistemadereservacionhotel.model.huesped.Huesped;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidad transaccional central del sistema. Representa el contrato legal y financiero
 * de alojamiento entre el hotel y el huésped. Consolida la asignación del recurso físico,
 * la temporalidad de la estadía y gestiona la propagación hacia los eventos operativos
 * (Check-In / Check-Out).
 */
@Entity
@Table(name = "reserva")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reserva extends Auditor<String> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Identificador único de la reserva a nivel de base de datos.
     * Clave primaria gestionada automáticamente por la estrategia de identidad.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReserva;

    /**
     * Localizador alfanumérico (Business Key) provisto al cliente para el seguimiento.
     * Restricción de unicidad estricta para garantizar indexación y búsquedas eficientes.
     */
    @NotBlank(message = "El código de reserva es obligatorio")
    @Column(unique = true, nullable = false, length = 100)
    private String codigoReserva;

    /**
     * Marca de tiempo exacta en la que se generó la solicitud original en el sistema.
     * Inmutable a nivel de esquema DDL (updatable = falsé) para auditoría financiera.
     */
    @NotNull(message = "La fecha de reserva es obligatoria")
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaReserva;

    /**
     * Límite inferior temporal en el que el cliente planea iniciar su ocupación.
     * Base lógica para el cálculo de disponibilidad del inventario físico.
     */
    @NotNull(message = "La fecha de entrada planificada es obligatoria")
    @Column(nullable = false)
    private LocalDate fechaEntradaPlanificada;

    /**
     * Límite superior temporal en el que el cliente planea liberar la habitación.
     * Requerido obligatoriamente para proyectar la liberación del recurso.
     */
    @NotNull(message = "La fecha de salida planificada es obligatoria")
    @Column(nullable = false)
    private LocalDate fechaSalidaPlanificada;

    /**
     * Proyección financiera del costo total de la estadía basada en la tarifa vigente.
     * Restringido a valores positivos. Mapeado con precisión monetaria (10,2) para
     * evitar discrepancias contables.
     */
    @NotNull(message = "El monto total es obligatorio")
    @PositiveOrZero(message = "El monto total no puede ser negativo")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montoTotalEstimado;

    /**
     * Recurso físico (Habitación) bloqueado por este contrato.
     * Relación de cardinalidad muchos a uno. Carga diferida (LAZY) obligatoria para
     * evitar cuellos de botella de rendimiento por consultas N+1.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_habitacion", nullable = false)
    private Habitacion habitacion;

    /**
     * Vínculo con el cliente titular que asume la responsabilidad financiera.
     * Relación de cardinalidad muchos a uno. Carga diferida (LAZY).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_huesped", nullable = false)
    private Huesped huesped;

    /**
     * Situación paramétrica actual en la máquina de estados operativos del hotel.
     * Relación de cardinalidad muchos a uno. Carga diferida (LAZY).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estado_reserva", nullable = false)
    private EstadoReserva estadoReserva;

    /**
     * Evento operativo de recepción física vinculado a esta reserva.
     * Relación uno a uno. La cascada completa (ALL) es aceptable aquí porque el Check-In
     * depende existencialmente de la reserva.
     */
    @OneToOne(mappedBy = "reserva", cascade = CascadeType.ALL)
    private CheckIn checkIn;

    /**
     * Evento operativo de finalización física vinculado a esta reserva.
     * Relación uno a uno.
     */
    @OneToOne(mappedBy = "reserva", cascade = CascadeType.ALL)
    private CheckOut checkOut;

    /**
     * Colección de individuos adicionales registrados bajo el mismo contrato de alojamiento.
     * Relación uno a muchos. Cascada completa (ALL) permitida al ser una entidad débil.
     */
    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL)
    private List<Acompanante> acompanantes;
}
