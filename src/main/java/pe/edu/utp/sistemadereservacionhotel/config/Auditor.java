package pe.edu.utp.sistemadereservacionhotel.config;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Clase base de auditoría para todas las entidades del sistema.
 * Registra automáticamente fechas de creación y modificación.
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditor<T> {

    @Column(name = "created_by", nullable = false, updatable = false)
    @CreatedDate
    private T createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdDate;

    @Column(name = "modified_by")
    @LastModifiedDate
    private T modifiedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_date")
    @LastModifiedDate
    private LocalDateTime modifiedDate;
}