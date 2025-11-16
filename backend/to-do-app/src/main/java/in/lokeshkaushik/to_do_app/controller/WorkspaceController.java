package in.lokeshkaushik.to_do_app.controller;

import in.lokeshkaushik.to_do_app.dto.WorkspaceCreateRequestDto;
import in.lokeshkaushik.to_do_app.dto.WorkspaceCreateResponseDto;
import in.lokeshkaushik.to_do_app.dto.WorkspaceResponseDto;
import in.lokeshkaushik.to_do_app.dto.WorkspaceIdsResponseDto;
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

    // post a workspace
    @PostMapping("")
    public ResponseEntity<WorkspaceCreateResponseDto> createWorkspace(@Valid @RequestBody WorkspaceCreateRequestDto workspaceDto){
        return ResponseEntity.ok(workspaceService.createWorkspace(workspaceDto));
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> workspaceExistsByName(@RequestParam String name){
        return ResponseEntity.ok(workspaceService.workspaceExistsByName(name));
    }

    // update a workspace

    // get task using taskId

    // post a task under a workspace

    // update a task under a workspace

}
