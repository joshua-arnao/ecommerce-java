package com.ecommerce.sportscenter.user.dto;

public record RegisterRequest(
        String firstName,
        String lastName,
        String dni,
        String email,
        String phoneNumber,
        String password
) {
}
