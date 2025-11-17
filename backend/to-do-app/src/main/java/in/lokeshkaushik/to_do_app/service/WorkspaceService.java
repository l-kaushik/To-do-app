package in.lokeshkaushik.to_do_app.service;

import in.lokeshkaushik.to_do_app.dto.TaskDto.TaskDto;
import in.lokeshkaushik.to_do_app.dto.TaskDto.TaskResponseDto;
import in.lokeshkaushik.to_do_app.dto.WorkspaceDtos.*;
import in.lokeshkaushik.to_do_app.exception.WorkspaceAlreadyExistsException;
import in.lokeshkaushik.to_do_app.exception.WorkspaceNotFoundException;
import in.lokeshkaushik.to_do_app.model.Task;
import in.lokeshkaushik.to_do_app.model.Workspace;
import in.lokeshkaushik.to_do_app.repository.WorkspaceRepository;
import jakarta.validation.Valid;
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
        return new WorkspaceResponseDto(workspace.getUuid(), workspace.getName(), fromTaskToTaskResponseDto(workspace.getTasks()));
    }

    // TODO: Add limit on how much ids can be fetched at once
    public WorkspaceIdsResponseDto getWorkspaces() {
       List<UUID> ids = workspaceRepository.findAllIdsByOwnerId(getUserId());
       return new WorkspaceIdsResponseDto(ids);
    }

    public WorkspaceCreateResponseDto createWorkspace(@Valid WorkspaceCreateRequestDto workspaceDto) {
        if(workspaceRepository.existsByName(workspaceDto.name())){
            throw new WorkspaceAlreadyExistsException("Cannot create another workspace with name: " + workspaceDto.name());
        }

        Workspace workspace = Workspace.builder()
                .owner(userService.getCurrentAuthenticatedUser())
                .name(workspaceDto.name())
                .build();

        // make sure each task has reference to workspace
        List<Task> tasks = fromAnyTaskDtoToTask(workspaceDto.tasks());
        tasks.forEach(task -> task.setWorkspace(workspace));
        workspace.setTasks(tasks);

        Workspace saved = workspaceRepository.save(workspace);

        // TODO: Handle it using custom exception and in UserService as well
        if(saved.getId() == null){
            throw new RuntimeException("Failed to save workspace");
        }

        return new WorkspaceCreateResponseDto(saved.getUuid(), saved.getName(), saved.getTasks().size());
    }

    public Boolean workspaceExistsByName(String name) {
        if(workspaceRepository.existsByName(name)) return true;
        throw new WorkspaceNotFoundException("Workspace not found with provided name: " + name);
    }

    public WorkspaceUpdateResponseDto updateWorkspace(@Valid WorkspaceUpdateRequestDto workspaceDto) {
        Workspace workspace = workspaceRepository.findByUuid(workspaceDto.uuid())
                .orElseThrow(() -> new WorkspaceNotFoundException(
                        "Workspace with UUID " + workspaceDto.uuid() + " was expected to exist but not found"
                ));

        // return same object if nothing to update
        if(workspace.getName().equals(workspaceDto.name()))
            return new WorkspaceUpdateResponseDto(workspace.getUuid(), workspace.getName());

        // update and return
        workspace.setName(workspaceDto.name());
        Workspace saved = workspaceRepository.save(workspace);
        return new WorkspaceUpdateResponseDto(saved.getUuid(), saved.getName());
    }

    private UUID getUserId(){
        return userService.getCurrentAuthenticatedUser().getUuid();
    }

    private List<TaskResponseDto> fromTaskToTaskResponseDto(List<Task> tasks){
        return tasks.stream().map(task -> new TaskResponseDto(
                        task.getUuid(),
                        task.getName(),
                        task.getDescription(),
                        task.isCompleted()
                )).toList();
    }

    private List<Task> fromAnyTaskDtoToTask(List<? extends TaskDto> tasks){
        return tasks.stream().map(task -> Task.builder()
                        .name(task.name())
                        .description(task.description())
                        .completed(task.completed())
                        .build()
                ).toList();
    }
}
