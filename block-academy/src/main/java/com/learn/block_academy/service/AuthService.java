package com.learn.block_academy.service;

import com.learn.block_academy.exception.ResourceNotFoundException;
import com.learn.block_academy.dto.LoginRequest;
import com.learn.block_academy.dto.RegisterRequest;
import com.learn.block_academy.dto.UserDto;
import com.learn.block_academy.model.User;
import com.learn.block_academy.model.UserProfile;
import com.learn.block_academy.repository.UserProfileRepository;
import com.learn.block_academy.repository.UserRepository;
import com.learn.block_academy.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public String login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return tokenProvider.generateToken(authentication);
    }

    @Transactional
    public UserDto register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException("Email is already taken");
        }

        // Create new user
        User user = User.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .fullName(registerRequest.getFullName())
               // .role(registerRequest.getRole())
                .enabled(true)
                .enrollments(new ArrayList<>())
                .build();

        User savedUser = userRepository.save(user);

        // Create user profile
        UserProfile profile = UserProfile.builder()
                .user(savedUser)
                .interests(new ArrayList<>())
                //.certificates(new ArrayList<>())
                .build();

        userProfileRepository.save(profile);

        return mapToUserDto(savedUser);
    }

    public UserDto getCurrentUser() throws ResourceNotFoundException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return mapToUserDto(user);
    }

    private UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                //.role(user.getRole())
                .build();
    }
}