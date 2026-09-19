package com.example.taskservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdatePasswordRequestDto (
        @NotBlank
        String oldPassword,

        @NotBlank
        @Size(min = 8)
        String newPassword
) {
}
