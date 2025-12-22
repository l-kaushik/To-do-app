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
@Table(name = "tasks",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"workspace_id", "rank"})
        }
)
public class Task extends AuditableEntity{
    @Column(nullable = false)
    private String rank;
    private String description;
    private boolean completed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;

    private Task(Builder builder) {
        setRank(builder.rank);
        setDescription(builder.description);
        setCompleted(builder.completed);
        setWorkspace(builder.workspace);
    }

    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {
        private String rank;
        private String description;
        private boolean completed;
        private Workspace workspace;

        private Builder() {
        }

        public Builder rank(String val) {
            rank = val;
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
