package com.example.taskservice.dto.response.user;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UserResponseSummaryDto(
        UUID id,
        String email,
        String firstName,
        String lastName,
        String profilePicturePresignedUrl
) {
}
