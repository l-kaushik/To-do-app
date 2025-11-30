package in.lokeshkaushik.to_do_app.controller;

import in.lokeshkaushik.to_do_app.dto.TaskDto.TaskCreateRequestDto;
import in.lokeshkaushik.to_do_app.dto.TaskDto.TaskListResponseDto;
import in.lokeshkaushik.to_do_app.dto.TaskDto.TaskResponseDto;
import in.lokeshkaushik.to_do_app.dto.TaskDto.TaskUpdateRequestDto;
import in.lokeshkaushik.to_do_app.dto.WorkspaceDtos.*;
import in.lokeshkaushik.to_do_app.service.WorkspaceService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/workspaces")
public class WorkspaceController {
    @Autowired
    WorkspaceService workspaceService;

    // Return ids of all workspaces under requested account
    @GetMapping("")
    public ResponseEntity<WorkspaceIdsResponseDto> getWorkspaces(){
        return ResponseEntity.ok(workspaceService.getWorkspaces());
    }

    // Return data of single workspace using uuid
    @GetMapping("/{workspaceId}")
    public ResponseEntity<WorkspaceResponseDto> getWorkspace(@PathVariable @NotNull UUID workspaceId){
        return ResponseEntity.ok(workspaceService.getWorkspace(workspaceId));
    }

    @PostMapping("")
    public ResponseEntity<WorkspaceCreateResponseDto> createWorkspace(@Valid @RequestBody WorkspaceCreateRequestDto workspaceDto){
        return ResponseEntity.ok(workspaceService.createWorkspace(workspaceDto));
    }

    @PutMapping("")
    public ResponseEntity<WorkspaceUpdateResponseDto> updateWorkspace(@Valid @RequestBody WorkspaceUpdateRequestDto workspaceDto){
        return ResponseEntity.ok(workspaceService.updateWorkspace(workspaceDto));
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> workspaceExistsByName(@RequestParam String name){
        return ResponseEntity.ok(workspaceService.workspaceExistsByName(name));
    }

    @GetMapping("/{workspaceId}/tasks/exists")
    public ResponseEntity<Boolean> taskExistsByName(@PathVariable @NotNull UUID workspaceId, @RequestParam String name){
        return ResponseEntity.ok(workspaceService.taskExistsByName(workspaceId, name));
    }

    @GetMapping("/{workspaceId}/tasks")
    public ResponseEntity<TaskListResponseDto> getTasks(@PathVariable @NotNull UUID workspaceId){
        return ResponseEntity.ok(workspaceService.getTasks(workspaceId));
    }

    @GetMapping("/{workspaceId}/tasks/{taskId}")
    public ResponseEntity<TaskResponseDto> getTask(@PathVariable @NotNull UUID workspaceId, @PathVariable @NotNull UUID taskId){
        return ResponseEntity.ok(workspaceService.getTask(workspaceId, taskId));
    }

    @PostMapping("/{workspaceId}/tasks")
    public ResponseEntity<TaskResponseDto> createTask(@PathVariable @NotNull UUID workspaceId,
                                                     @Valid @RequestBody TaskCreateRequestDto taskCreateRequestDto){
        return ResponseEntity.ok(workspaceService.createTask(workspaceId, taskCreateRequestDto));
    }

    @PutMapping("/{workspaceId}/tasks")
    public ResponseEntity<TaskResponseDto> updateTask(@PathVariable @NotNull UUID workspaceId,
                                                      @Valid @RequestBody TaskUpdateRequestDto taskUpdateRequestDto){
        return ResponseEntity.ok(workspaceService.updateTask(workspaceId, taskUpdateRequestDto));
    }

}
