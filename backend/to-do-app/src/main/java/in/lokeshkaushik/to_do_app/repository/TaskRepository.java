package in.lokeshkaushik.to_do_app.repository;

import in.lokeshkaushik.to_do_app.dto.TaskDto.TaskResponseDto;
import in.lokeshkaushik.to_do_app.model.Task;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByUuidAndWorkspaceUuidAndWorkspaceOwnerUuid(UUID uuid, UUID workspaceUuid, UUID ownerUuid);

    @Query("""
            SELECT new in.lokeshkaushik.to_do_app.dto.TaskDto.TaskResponseDto(
            t.uuid, t.rank, t.description, t.completed
            )
            FROM Task t
            JOIN t.workspace w
            WHERE w.uuid = :workspaceUuid
            AND w.owner.uuid = :ownerUuid
            """)
    Page<TaskResponseDto> findAllWithWorkspaceUuid(Pageable pageable, @Param("workspaceUuid") UUID workspaceUuid,
                                                   @Param("ownerUuid") UUID ownerUuid);

    Optional<Task> findTopByWorkspaceUuidAndWorkspaceOwnerUuidOrderByRankDesc(UUID workspaceUuid, UUID ownerUuid);
    boolean existsByRankAndWorkspaceUuidAndWorkspaceOwnerUuid(String rank, UUID workspaceUuid, UUID ownerUuid);
    long deleteByUuidAndWorkspaceUuidAndWorkspaceOwnerUuid(@NotNull UUID uuid, @NotNull UUID workspaceUuid, UUID ownerUuid);
}
