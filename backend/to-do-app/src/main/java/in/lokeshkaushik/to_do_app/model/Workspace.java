package in.lokeshkaushik.to_do_app.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "workspaces")
public class Workspace extends AuditableEntity{
    // many workspaces belong to one user
    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(unique = true, nullable = false)
    private String name;

    // one workspace can have many tasks
    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

    private Workspace(Builder builder) {
        setOwner(builder.owner);
        setName(builder.name);
        setTasks(builder.tasks);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private User owner;
        private String name;
        private List<Task> tasks;

        private Builder() {
        }

        public Builder owner(User val) {
            owner = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder tasks(List<Task> val) {
            tasks = val;
            return this;
        }

        public Workspace build() {
            return new Workspace(this);
        }
    }
}
