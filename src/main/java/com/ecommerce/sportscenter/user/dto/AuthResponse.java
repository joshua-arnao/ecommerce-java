package com.ecommerce.sportscenter.user.dto;

import java.util.UUID;

public record AuthResponse(
        String token,
        UUID userId,
        String firstName,
        String email
) {
}
