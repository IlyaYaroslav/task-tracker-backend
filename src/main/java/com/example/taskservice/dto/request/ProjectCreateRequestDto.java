package com.example.taskservice.dto.request;

import lombok.Builder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

@Builder
public record ProjectCreateRequestDto(
        UUID userId,

        @NotBlank @Size(max = 100)
        String name,
        @Size(max = 2000)
        String description,
        @NotBlank
        @Pattern(regexp = "[A-Za-z][A-Za-z0-9_-]{1,9}", message = "must contain 2-10 letters, digits, '_' or '-'")
        String key,
        List<@NotNull UUID> projectMembers
) {
}
