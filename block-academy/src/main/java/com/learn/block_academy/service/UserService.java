package com.learn.block_academy.service;

import com.learn.block_academy.dto.UserDto;
import com.learn.block_academy.exception.ResourceNotFoundException;
import com.learn.block_academy.model.User;
import com.learn.block_academy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDto getUserById(Long id) throws ResourceNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return mapToUserDto(user);
    }

    public Page<UserDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(this::mapToUserDto);
    }

    public UserDto updateUser(Long id, UserDto userDto) throws ResourceNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setFullName(userDto.getFullName());

//        if (userDto.getRole() != null) {
//            user.setRole(userDto.getRole());
//        }

        User updatedUser = userRepository.save(user);
        return mapToUserDto(updatedUser);
    }

    public void deleteUser(Long id) throws ResourceNotFoundException {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    public void changePassword(Long id, String currentPassword, String newPassword) throws ResourceNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
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
