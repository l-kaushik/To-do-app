package in.lokeshkaushik.to_do_app.repository;

import in.lokeshkaushik.to_do_app.model.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
    @Query("SELECT w.uuid FROM Workspace w WHERE w.owner.uuid = :userId")
    List<UUID> findAllIdsByOwnerId(@Param("userId") UUID userId);

    boolean existsByName(String name);
    boolean existsByUuid(UUID uuid);

    Optional<Workspace> findByUuid(UUID uuid);
}
