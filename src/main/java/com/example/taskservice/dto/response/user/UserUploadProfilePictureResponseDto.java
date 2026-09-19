package com.example.taskservice.dto.response.user;

import java.util.UUID;

public record UserUploadProfilePictureResponseDto(
        UUID id,
        String profilePicturePresignedUrl
) {
}
