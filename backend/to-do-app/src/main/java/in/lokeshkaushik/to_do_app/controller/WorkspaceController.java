package in.lokeshkaushik.to_do_app.controller;

import in.lokeshkaushik.to_do_app.dto.WorkspaceResponseDto;
import in.lokeshkaushik.to_do_app.dto.WorkspaceIdsResponseDto;
import in.lokeshkaushik.to_do_app.service.WorkspaceService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workspaces")
public class WorkspaceController {
    @Autowired
    WorkspaceService workspaceService;

    // Return ids of all workspaces under request account
    @GetMapping("")
    public ResponseEntity<WorkspaceIdsResponseDto> getWorkspaces(){
        return ResponseEntity.ok(workspaceService.getWorkspaces());
    }

    // Return data of single workspace using id
    @GetMapping("/{workspaceId}")
    public ResponseEntity<WorkspaceResponseDto> getWorkspace(@PathVariable @NotNull UUID workspaceId){
        return ResponseEntity.ok(workspaceService.getWorkspace(workspaceId));
    }
}
