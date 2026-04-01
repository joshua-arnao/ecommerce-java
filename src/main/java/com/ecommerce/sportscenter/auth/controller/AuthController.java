package com.ecommerce.sportscenter.auth.controller;

import com.ecommerce.sportscenter.auth.dto.JwtRequest;
import com.ecommerce.sportscenter.auth.dto.JwtResponse;
import com.ecommerce.sportscenter.shared.security.JwtService;
import com.ecommerce.sportscenter.user.dto.AuthResponse;
import com.ecommerce.sportscenter.user.dto.LoginRequest;
import com.ecommerce.sportscenter.user.dto.RegisterRequest;
import com.ecommerce.sportscenter.user.dto.UserResponse;
import com.ecommerce.sportscenter.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
