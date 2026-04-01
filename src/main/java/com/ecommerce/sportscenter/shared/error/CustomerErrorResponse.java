package com.ecommerce.sportscenter.shared.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record CustomerErrorResponse (
        int status,
        String message,
        String path,
        LocalDateTime timestamp
) {
}