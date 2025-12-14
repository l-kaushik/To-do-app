package in.lokeshkaushik.to_do_app.repository;

import in.lokeshkaushik.to_do_app.dto.WorkspaceDtos.WorkspaceMetaDto;
import in.lokeshkaushik.to_do_app.model.Workspace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    boolean existsByNameAndOwnerUuid(String name, UUID ownerUuid);
    boolean existsByUuidAndOwnerUuid(UUID uuid, UUID ownerUuid);

    Optional<Workspace> findByUuidAndOwnerUuid(UUID uuid, UUID ownerUuid);

    @Query("""
            SELECT new in.lokeshkaushik.to_do_app.dto.WorkspaceDtos.WorkspaceMetaDto(
                    w.uuid, w.name, SIZE(w.tasks)
            )
            FROM Workspace w
            WHERE w.owner.uuid = :userUuid
        """)
    Page<WorkspaceMetaDto> findAllWithTaskCount(Pageable pageable, @Param("userUuid") UUID userUuid);

    @Query("""
            SELECT new in.lokeshkaushik.to_do_app.dto.WorkspaceDtos.WorkspaceMetaDto(
                w.uuid, w.name, SIZE(w.tasks)
            )
            FROM Workspace w
            WHERE w.uuid = :uuid
            AND w.owner.uuid = :userUuid
            """)
    Optional<WorkspaceMetaDto> findWorkspaceMeta(@Param("uuid") UUID uuid, @Param("userUuid") UUID userUuid);
}
