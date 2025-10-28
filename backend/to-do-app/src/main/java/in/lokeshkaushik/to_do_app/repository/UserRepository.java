package in.lokeshkaushik.to_do_app.repository;

import in.lokeshkaushik.to_do_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmailId(String emailId);
    boolean existsByUsername(String username);
}
