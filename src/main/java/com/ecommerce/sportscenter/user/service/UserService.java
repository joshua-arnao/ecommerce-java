package com.ecommerce.sportscenter.user.service;

import com.ecommerce.sportscenter.user.dto.AuthResponse;
import com.ecommerce.sportscenter.user.dto.LoginRequest;
import com.ecommerce.sportscenter.user.dto.RegisterRequest;
import com.ecommerce.sportscenter.user.dto.UserResponse;

public interface UserService {
    UserResponse createUser(RegisterRequest registerRequest);
    AuthResponse login(LoginRequest loginRequest);
    UserResponse findByEmail(String email);
}
