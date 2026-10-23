package pe.edu.utp.sistemadereservacionhotel.config;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Clase base de auditoría para todas las entidades del sistema.
 * Registra automáticamente fechas de creación y modificación.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class Auditor<T> {

    @Column(name = "created_by", nullable = false, updatable = false)
    private T createdBy;

    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "modified_by")
    private T modifiedBy;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @SuppressWarnings("unchecked")
    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
        createdBy = (T) "system";
    }

    @SuppressWarnings("unchecked")
    @PreUpdate
    protected void onUpdate() {
        modifiedDate = LocalDateTime.now();
        modifiedBy = (T) "system";
    }
}
