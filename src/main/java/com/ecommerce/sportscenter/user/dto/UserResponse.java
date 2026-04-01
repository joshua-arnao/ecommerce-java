package com.ecommerce.sportscenter.user.dto;

import java.util.UUID;

public record UserResponse(
        UUID userId,
        String firstName,
        String lastName,
        String email,
        String phoneNumber
) {
}
