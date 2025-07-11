package com.david.savetodouserservice.User;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

public record ValidationErrorResponseDto(
        String apiPath,
        HttpStatus errorCode,
        Map<String, Set<String>> fieldsErrors,
        LocalDateTime timestamp
) {
}
