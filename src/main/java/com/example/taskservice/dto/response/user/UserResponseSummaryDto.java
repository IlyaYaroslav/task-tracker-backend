package com.example.taskservice.dto.response.user;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UserResponseSummaryDto(
        UUID id,
        String email,
        UserRole role,
        String firstName,
        String lastName,
        String profilePicturePresignedUrl
) {
}
