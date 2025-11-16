package in.lokeshkaushik.to_do_app.service;

import in.lokeshkaushik.to_do_app.dto.WorkspaceResponseDto;
import in.lokeshkaushik.to_do_app.dto.WorkspaceIdsResponseDto;
import in.lokeshkaushik.to_do_app.exception.WorkspaceNotFoundException;
import in.lokeshkaushik.to_do_app.model.Workspace;
import in.lokeshkaushik.to_do_app.repository.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WorkspaceService {

    @Autowired
    UserService userService;

    @Autowired
    WorkspaceRepository workspaceRepository;

    public WorkspaceResponseDto getWorkspace(UUID workspaceId) {
        Workspace workspace = workspaceRepository.findByUuid(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException("Workspace is not present or invalid UUID provided."));

        // TODO: Add limit on how much tasks can be fetched at once
        return new WorkspaceResponseDto(workspace.getUuid(), workspace.getName(), workspace.getTasks());
    }

    // TODO: Add limit on how much ids can be fetched at once
    public WorkspaceIdsResponseDto getWorkspaces() {
       List<UUID> ids = workspaceRepository.findAllIdsByOwnerId(getUserId());
       return new WorkspaceIdsResponseDto(ids);
    }

    public Boolean workspaceExistsByName(String name) {
        if(workspaceRepository.existsByName(name)) return true;
        throw new WorkspaceNotFoundException("Workspace not found with provided name: " + name);
    }

    private UUID getUserId(){
        return userService.getCurrentAuthenticatedUser().getUuid();
    }
}
