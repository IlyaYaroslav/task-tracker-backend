package com.example.taskservice.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UserLogInResponseDto(
        UUID id,
        String accessToken
) {
}
