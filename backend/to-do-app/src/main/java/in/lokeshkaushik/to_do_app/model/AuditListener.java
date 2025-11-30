package in.lokeshkaushik.to_do_app.model;


import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.Instant;

public final class AuditListener {
    @PrePersist
    protected void setCreatedAt(Object entity) {
        if(entity instanceof Auditable auditable){
            auditable.setCreatedAt(Instant.now());
            auditable.setUpdatedAt(Instant.now());
        }
    }

    @PreUpdate
    protected void setUpdatedAt(Object entity) {
        if(entity instanceof Auditable auditable) {
            auditable.setUpdatedAt(Instant.now());
        }
    }
}
