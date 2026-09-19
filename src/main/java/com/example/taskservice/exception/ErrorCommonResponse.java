package com.example.taskservice.exception;

import lombok.Builder;

@Builder
public record ErrorCommonResponse(


        String errorMessage
) {
}
