package com.ecommerce.sportscenter.user.service;

import com.ecommerce.sportscenter.shared.exceptions.InvalidCredentialsException;
import com.ecommerce.sportscenter.shared.exceptions.UserAlreadyExistsException;
import com.ecommerce.sportscenter.shared.exceptions.UserNotFoundException;
import com.ecommerce.sportscenter.shared.security.JwtService;
import com.ecommerce.sportscenter.user.dto.AuthResponse;
import com.ecommerce.sportscenter.user.dto.LoginRequest;
import com.ecommerce.sportscenter.user.dto.RegisterRequest;
import com.ecommerce.sportscenter.user.dto.UserResponse;
import com.ecommerce.sportscenter.user.entity.User;
import com.ecommerce.sportscenter.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public UserResponse createUser(RegisterRequest registerRequest) {
//        boolean userExist = userRepository.findByEmail(registerRequest.email()).isPresent();
//
//        if(userExist) {
//           throw new UserAlreadyExistsException("Invalid credentials");
//        }
        userRepository.findByEmail(registerRequest.email())
                .ifPresent(user -> {throw new UserAlreadyExistsException("Invalid credentials");});

        String hashedPassword = passwordEncoder.encode(registerRequest.password());

        User user = User.builder()
                .firstName(registerRequest.firstName())
                .lastName(registerRequest.lastName())
                .dni(registerRequest.dni())
                .email(registerRequest.email())
                .phoneNumber(registerRequest.phoneNumber())
                .password(hashedPassword)
                .build();

        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getUserId(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getEmail(),
                savedUser.getPhoneNumber()
        );

    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.email()).orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));

        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        String token = jwtService.generateToken(user);

        return new AuthResponse(
                token,
                user.getUserId(),
                user.getFirstName(),
                user.getEmail()
        );
    }

    @Override
    public UserResponse findByEmail(String email) {
//        Optional<User> optionalUser = userRepository.findByEmail(email);
//
//        return optionalUser.map(user -> new UserResponse(
//                user.getUserId(),
//                user.getFirstName(),
//                user.getLastName(),
//                user.getEmail(),
//                user.getPhoneNumber()
//        )).orElseThrow(() -> new UserNotFoundException("Invalid Credentials"));

        return userRepository.findByEmail(email)
                .map(user -> new UserResponse(
                        user.getUserId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getPhoneNumber()
                ))
                .orElseThrow(() -> new UserNotFoundException("Invalid Credentials"));

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Invalid credentials"));
    }
}
