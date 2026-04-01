package com.ecommerce.sportscenter.user.dto;

public record LoginRequest(
        String email,
        String password
) {
}
