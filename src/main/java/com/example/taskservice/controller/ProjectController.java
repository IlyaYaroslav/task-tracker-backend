package com.example.taskservice.controller;

import com.example.taskservice.dto.request.ProjectCreateRequestDto;
import com.example.taskservice.dto.response.ProjectCreateResponseDto;
import com.example.taskservice.dto.response.ProjectResponseDto;
import com.example.taskservice.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequestMapping("/projects")
@RequiredArgsConstructor
@RestController
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectCreateResponseDto> create(
            @Valid @RequestBody ProjectCreateRequestDto projectCreateRequestDto,
            @AuthenticationPrincipal UUID ownerId
    ) {
        ProjectCreateResponseDto project = projectService.create(projectCreateRequestDto);

        return ResponseEntity.status(201)
                .body(project);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> delete(@PathVariable UUID projectId, @AuthenticationPrincipal UUID ownerId) {
        projectService.deleteProjectById(projectId, ownerId);
        return ResponseEntity.status(204).build();
    }

    @GetMapping
    public ResponseEntity<ProjectResponseDto> getProjectByUserId(@AuthenticationPrincipal UUID ownerId) {
        return ResponseEntity.ok(projectService.getProjectsByUserId(ownerId));
    }
}
