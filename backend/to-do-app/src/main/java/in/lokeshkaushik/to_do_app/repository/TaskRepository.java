package in.lokeshkaushik.to_do_app.repository;

import in.lokeshkaushik.to_do_app.dto.TaskDto.TaskResponseDto;
import in.lokeshkaushik.to_do_app.model.Task;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    @Query("""
            SELECT new in.lokeshkaushik.to_do_app.dto.TaskDto.TaskResponseDto(
            t.uuid, t.name, t.description, t.completed
            )
            FROM Task t
            JOIN t.workspace w
            WHERE w.uuid = :workspaceUuid
            """)
    Page<TaskResponseDto> findAllWithWorkspaceUuid(@NotNull UUID workspaceUuid, Pageable pageable);
}
