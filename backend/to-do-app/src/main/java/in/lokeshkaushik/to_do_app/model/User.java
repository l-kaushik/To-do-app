package in.lokeshkaushik.to_do_app.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends AuditableEntity{

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String emailId;

    @Column(nullable = false)
    private String password;

    private User(Builder builder) {
        setUsername(builder.username);
        setEmailId(builder.emailId);
        setPassword(builder.password);
    }

    public static Builder builder() {
        return new Builder();
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
