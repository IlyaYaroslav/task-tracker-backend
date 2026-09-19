package com.example.taskservice.service;

import com.example.taskservice.dto.request.ProjectCreateRequestDto;
import com.example.taskservice.dto.response.ProjectCreateResponseDto;
import com.example.taskservice.dto.response.ProjectResponseDto;
import com.example.taskservice.exception.ProjectKeyAlreadyExistsException;
import com.example.taskservice.mapper.ProjectMapper;
import com.example.taskservice.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    @Transactional
    public ProjectCreateResponseDto create(ProjectCreateRequestDto request, UUID ownerId) {
        String normalizedKey = request.key().trim().toUpperCase(Locale.ROOT);
        if (projectRepository.existsByKeyIgnoreCase(normalizedKey)) {
            throw new ProjectKeyAlreadyExistsException(normalizedKey);
        }

        ProjectCreateRequestDto normalizedRequest = new ProjectCreateRequestDto(
                request.name().trim(),
                request.description(),
                normalizedKey,
                request.projectMembers()
        );
        var project = projectMapper.toEntity(normalizedRequest);
        project.setOwnerId(ownerId);
        project.setMemberIds(new LinkedHashSet<>());
        if (request.projectMembers() != null) {
            project.getMemberIds().addAll(request.projectMembers());
        }
        if (ownerId != null) {
            project.getMemberIds().add(ownerId);
        }

        return projectMapper.toDto(projectRepository.save(project));
    }

    @Transactional
    public void deleteProjectById(UUID projectId, UUID ownerId) {
        var project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        if (!Objects.equals(project.getOwnerId(), ownerId)) {
            throw new AccessDeniedException("Only the owner can delete this project");
        }
        projectRepository.delete(project);
    }

    public ProjectCreateResponseDto getProjectByUserId(UUID projectId) {
        return null;
    }

    public ProjectResponseDto getProjectsByUserId(UUID ownerId) {
        return projectMapper.toDto(ownerId, projectRepository.findProjectsByOwnerId(ownerId));
    }
}
