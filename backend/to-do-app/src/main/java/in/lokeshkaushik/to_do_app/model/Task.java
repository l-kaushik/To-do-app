package in.lokeshkaushik.to_do_app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tasks")
public class Task extends AuditableEntity{
    @Column(nullable = false)
    private String name;
    private String description;
    private boolean completed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;

    private Task(Builder builder) {
        setName(builder.name);
        setDescription(builder.description);
        setCompleted(builder.completed);
        setWorkspace(builder.workspace);
    }

    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {
        private String name;
        private String description;
        private boolean completed;
        private Workspace workspace;

        private Builder() {
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public Builder completed(boolean val) {
            completed = val;
            return this;
        }

        public Builder workspace(Workspace val) {
            workspace = val;
            return this;
        }

        public Task build() {
            return new Task(this);
        }
    }
}
