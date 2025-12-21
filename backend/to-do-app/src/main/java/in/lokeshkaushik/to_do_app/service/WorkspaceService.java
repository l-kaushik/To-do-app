package in.lokeshkaushik.to_do_app.service;

import in.lokeshkaushik.to_do_app.dto.TaskDto.*;
import in.lokeshkaushik.to_do_app.dto.WorkspaceDtos.*;
import in.lokeshkaushik.to_do_app.exception.*;
import in.lokeshkaushik.to_do_app.model.Task;
import in.lokeshkaushik.to_do_app.model.Workspace;
import in.lokeshkaushik.to_do_app.repository.TaskRepository;
import in.lokeshkaushik.to_do_app.repository.WorkspaceRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WorkspaceService {

    @Autowired
    UserService userService;

    @Autowired
    WorkspaceRepository workspaceRepository;

    @Autowired
    TaskRepository taskRepository;

    public WorkspaceMetaDto getWorkspace(UUID workspaceId) {
        return workspaceRepository.findWorkspaceMeta(workspaceId, getUserId())
                .orElseThrow(() ->
                        new WorkspaceNotFoundException("Workspace is not present or invalid UUID provided."));
    }

    // TODO: Add limit on how much ids can be fetched at once
    public WorkspaceIdsResponseDto getWorkspaces() {
       List<UUID> ids = workspaceRepository.findAllIdsByOwnerId(getUserId());
       return new WorkspaceIdsResponseDto(ids);
    }

    public Page<WorkspaceMetaDto> getFullWorkspaces(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return workspaceRepository.findAllWithTaskCount(pageable, getUserId());
    }

    public WorkspaceCreateResponseDto createWorkspace(@Valid WorkspaceCreateRequestDto workspaceDto) {
        if(workspaceRepository.existsByNameAndOwnerUuid(workspaceDto.name(), getUserId())){
            throw new WorkspaceAlreadyExistsException("Cannot create another workspace with name: " + workspaceDto.name());
        }

        Workspace workspace = Workspace.builder()
                .owner(userService.getCurrentAuthenticatedUser())
                .name(workspaceDto.name())
                .build();

        // make sure each task has reference to workspace
        List<Task> tasks = fromAnyTaskDtoToTask(workspaceDto.tasks());
        tasks.forEach(task -> {
            var lastRank = generateNextRank(workspace.getUuid());
            task.setRank(lastRank);
            task.setWorkspace(workspace);
        });
        workspace.setTasks(tasks);

        Workspace saved = workspaceRepository.save(workspace);

        if(saved.getId() == null){
            throw new SaveFailedException("Failed to save workspace");
        }

        return new WorkspaceCreateResponseDto(saved.getUuid(), saved.getName(), saved.getTasks().size());
    }

    public Boolean workspaceExistsByName(String name) {
        return workspaceRepository.existsByNameAndOwnerUuid(name, getUserId());
    }

    public WorkspaceUpdateResponseDto updateWorkspace(@Valid WorkspaceUpdateRequestDto workspaceDto) {
        Workspace workspace = workspaceRepository.findByUuidAndOwnerUuid(workspaceDto.uuid(), getUserId())
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

    public TaskResponseDto getTask(@NotNull UUID workspaceId, @NotNull UUID taskId) {
        verifyWorkspaceExists(workspaceId);

        Task task = taskRepository.findByUuidAndWorkspaceUuidAndWorkspaceOwnerUuid(taskId, workspaceId, getUserId())
                .orElseThrow(() -> new TaskNotFoundException("Task with UUID " + taskId + " was not found."));

        return fromTaskToTaskResponseDto(List.of(task)).getFirst();
    }

    public Page<TaskResponseDto> getTasks(@NotNull UUID workspaceId, int page, int size) {
        verifyWorkspaceExists(workspaceId);

        Pageable pageable = PageRequest.of(page, size);
        return taskRepository.findAllWithWorkspaceUuid(pageable, workspaceId, getUserId());
    }

    public TaskResponseDto createTask(@NotNull UUID workspaceId, TaskCreateRequestDto taskCreateRequestDto) {
        verifyWorkspaceExists(workspaceId);

        var rank = generateNextRank(workspaceId);
        verifyGeneratedRank(rank, workspaceId);
        var description = taskCreateRequestDto.description();
        var completed = taskCreateRequestDto.completed();

        Workspace workspace = workspaceRepository.findByUuidAndOwnerUuid(workspaceId, getUserId()).orElseThrow(IllegalStateException::new);

        Task task = Task.builder().rank(rank).description(description).completed(completed).workspace(workspace).build();
        Task saved = taskRepository.save(task);
        if(saved.getId() == null){
            throw new SaveFailedException("Failed to save task");
        }

        return fromTaskToTaskResponseDto(List.of(saved)).getFirst();
    }

    public TaskResponseDto updateTask(@NotNull UUID workspaceId, @Valid TaskUpdateRequestDto taskUpdateRequestDto) {
        verifyWorkspaceExists(workspaceId);

        var beforeRank = taskUpdateRequestDto.beforeRank();
        var afterRank = taskUpdateRequestDto.afterRank();

        verifyTaskExistsByRank(beforeRank, workspaceId);
        verifyTaskExistsByRank(afterRank, workspaceId);

        var uuid = taskUpdateRequestDto.uuid();
        var rank = generateNextRank(beforeRank, afterRank);
        verifyGeneratedRank(rank, workspaceId);
        var description = taskUpdateRequestDto.description();
        var completed = taskUpdateRequestDto.completed();

        Task task = taskRepository.findByUuidAndWorkspaceUuidAndWorkspaceOwnerUuid(uuid, workspaceId, getUserId())
                .orElseThrow(() -> new TaskNotFoundException("Task with UUID " + uuid + " was expected to exist but not found"));

        if(!task.getRank().equals(rank)) task.setRank(rank);
        if(!task.getDescription().equals(description)) task.setDescription(description);
        if(task.isCompleted() != completed) task.setCompleted(completed);

        Task saved = taskRepository.save(task);
        return fromTaskToTaskResponseDto(List.of(saved)).getFirst();
    }

    private UUID getUserId(){
        return userService.getCurrentAuthenticatedUser().getUuid();
    }

    private void verifyWorkspaceExists(UUID workspaceId) {
        if(!workspaceRepository.existsByUuidAndOwnerUuid(workspaceId, getUserId()))
            throw new WorkspaceNotFoundException("Workspace with UUID " + workspaceId + " was not found.");
    }

    private void verifyTaskExistsByRank(String rank, UUID workspaceUuid){
        if(!taskRepository.existsByRankAndWorkspaceUuidAndWorkspaceOwnerUuid(rank, workspaceUuid, getUserId()))
            throw new TaskNotFoundException("Task with rank " + rank + " was not found");
    }

    private void verifyGeneratedRank( String rank, UUID workspaceId){
        if(taskRepository.existsByRankAndWorkspaceUuidAndWorkspaceOwnerUuid(rank, workspaceId, getUserId()))
            throw new BadRequestException("Invalid ranks provided!");
    }

    private String generateNextRank(String left, String right){
        return calculateLexRankMidPoint(left, right);
    }

    private String generateNextRank(UUID workspaceId) {
        Optional<Task> lastTask = taskRepository.findTopByWorkspaceUuidAndWorkspaceOwnerUuidOrderByRankDesc(workspaceId, getUserId());

        if(lastTask.isPresent()){
             return calculateLexRankMidPoint(lastTask.get().getRank(), "z".repeat(lastTask.get().getRank().length()));
        }
        return "1000";
    }

    private static String calculateLexRankMidPoint(String left, String right) {
        if (left.compareTo(right) >= 0) throw new IllegalArgumentException("Invalid ranks provided!");
        String alphanumeric = "0123456789abcdefghijklmnopqrstuvwxyz";
        StringBuilder st = new StringBuilder();
        int i = 0;
        boolean foundMidPoint = false;
        while(i < left.length() || i < right.length()){
            char leftChar = (i < left.length()) ? left.charAt(i) : '0';
            char rightChar = (i < right.length()) ? right.charAt(i) : 'z';
            int L = alphanumeric.indexOf(leftChar);
            int R = alphanumeric.indexOf(rightChar);

            if((R - L) == 1 || (R - L) == 0){
                i++;
                st.append(leftChar);
                continue;
            }
            else if((R - L) > 1){
                int midDigit = ((L + R)/2);
                st.append(alphanumeric.charAt(midDigit));
                foundMidPoint = true;
                break;
            }
            i++;
        }
        if(!foundMidPoint) st.append(alphanumeric.charAt(alphanumeric.length()/2));
        return st.toString();
    }



    private List<TaskResponseDto> fromTaskToTaskResponseDto(List<Task> tasks){
        return tasks.stream().map(task -> new TaskResponseDto(
                        task.getUuid(),
                        task.getRank(),
                        task.getDescription(),
                        task.isCompleted()
                )).toList();
    }

    private List<Task> fromAnyTaskDtoToTask(List<? extends TaskDto> tasks){
        return tasks.stream().map(task -> Task.builder()
                        .description(task.description())
                        .completed(task.completed())
                        .build()
                ).toList();
    }
}
