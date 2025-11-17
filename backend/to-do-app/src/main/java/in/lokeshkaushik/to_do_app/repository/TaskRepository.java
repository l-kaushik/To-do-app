package in.lokeshkaushik.to_do_app.repository;

import in.lokeshkaushik.to_do_app.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    boolean existsByNameAndWorkspaceUuid(String name, UUID workspaceUuid);
    boolean existsByUuid(UUID uuid);

    Optional<Task> findByUuid(UUID uuid);
    Optional<Task> findByNameAndWorkspaceUuid(String name, UUID workspaceUuid);
    List<Task> findAllByWorkspaceUuid(UUID workspaceUuid);
}
