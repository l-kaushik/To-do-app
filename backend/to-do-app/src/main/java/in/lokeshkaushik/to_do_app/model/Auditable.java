package in.lokeshkaushik.to_do_app.model;

import java.time.Instant;

public interface Auditable {
    Instant getCreatedAt();
    Instant getUpdatedAt();

    void setCreatedAt(Instant createdAt);
    void setUpdatedAt(Instant updatedAt);
}
