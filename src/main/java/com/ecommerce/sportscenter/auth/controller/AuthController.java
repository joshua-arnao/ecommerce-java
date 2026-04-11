package com.ecommerce.sportscenter.auth.controller;

import com.ecommerce.sportscenter.user.dto.AuthResponse;
import com.ecommerce.sportscenter.user.dto.LoginRequest;
import com.ecommerce.sportscenter.user.dto.RegisterRequest;
import com.ecommerce.sportscenter.user.dto.UserResponse;
import com.ecommerce.sportscenter.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public UserResponse registerUser (@Valid @RequestBody RegisterRequest registerRequest) {
        return userService.createUser(registerRequest);
    }

    @PostMapping("/login")
    public AuthResponse loginUser (@Valid @RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }
}
