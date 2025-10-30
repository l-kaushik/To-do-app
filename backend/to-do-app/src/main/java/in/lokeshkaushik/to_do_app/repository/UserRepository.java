package in.lokeshkaushik.to_do_app.repository;

import in.lokeshkaushik.to_do_app.model.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmailId(String emailId);
    boolean existsByUsername(String username);

    // Optional prevents NullPointerException in case user not found
    Optional<User> findByUuid(UUID uuid);
    Optional<User> findByEmailId(String identifier);
    Optional<User> findByUsername(String identifier);
}
