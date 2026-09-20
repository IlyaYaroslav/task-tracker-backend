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
    public ProjectCreateResponseDto create(ProjectCreateRequestDto request) {
        String normalizedKey = request.key().trim().toUpperCase(Locale.ROOT);
        if (projectRepository.existsByKeyIgnoreCase(normalizedKey)) {
            throw new ProjectKeyAlreadyExistsException(normalizedKey);
        }

        return null;
    }

    @Transactional
    public void deleteProjectById(UUID projectId, UUID ownerId) {

    }

    public ProjectCreateResponseDto getProjectByUserId(UUID projectId) {
        return null;
    }

    public ProjectResponseDto getProjectsByUserId(UUID ownerId) {
        return null;
    }
}
