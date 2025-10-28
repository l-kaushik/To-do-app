package in.lokeshkaushik.to_do_app.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(nullable = false, unique = true, updatable = false)
    private UUID uuid = UUID.randomUUID();

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String emailId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    private User(Builder builder) {
        setUsername(builder.username);
        setEmailId(builder.emailId);
        setPassword(builder.password);
    }

    public static Builder builder() {
        return new Builder();
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = Instant.now();
    }

    public static final class Builder {
        private String username;
        private String emailId;
        private String password;

        private Builder() {
        }

        public Builder username(String val) {
            username = val;
            return this;
        }

        public Builder emailId(String val) {
            emailId = val;
            return this;
        }

        public Builder password(String val) {
            password = val;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
