package com.example.taskservice.dto.response;

import java.time.Instant;
import java.util.UUID;

public record ProjectSummaryResponseDto (
        UUID id,
        String name,
        String key,
        String description,
        Instant createdAt,
        Instant updatedAt

){
}
