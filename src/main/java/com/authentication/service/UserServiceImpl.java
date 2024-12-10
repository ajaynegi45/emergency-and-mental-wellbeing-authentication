package com.authentication.service;

import com.authentication.config.PasswordEncoder;
import com.authentication.dto.UserLoginRequest;
import com.authentication.dto.UserRegistrationRequest;
import com.authentication.dto.UserResponse;
import com.authentication.dto.UserUpdateRequest;
import com.authentication.entity.Role;
import com.authentication.entity.User;
import com.authentication.exception.UserAlreadyExistsException;
import com.authentication.exception.UserNotFoundException;
import com.authentication.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse registerUser(UserRegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email is already registered.");
        }
        String encoded_password = passwordEncoder.bCryptPasswordEncoder().encode(request.getPassword());
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setRole(Role.USER.name());
        user.setPassword(encoded_password);
        userRepository.save(user);
        return toUserResponse(user);
    }



    @Override
    public UserResponse loginUser(UserLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("Invalid email."));

        if (!(passwordEncoder.bCryptPasswordEncoder().matches(request.getPassword(), user.getPassword()))){
            throw new UserNotFoundException("Invalid password.");
        }

        user.setLastLogin(java.time.LocalDateTime.now());
        userRepository.save(user);
        UserResponse userResp=toUserResponse(user);
        return userResp;
    }

    private UserResponse toUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setUserId(user.getUserId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        return response;
    }



    public UserResponse updateUserProfile(UserUpdateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (request.getName() != null) user.setName(request.getName());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getPassword() != null) {
            String encodedPassword = passwordEncoder.bCryptPasswordEncoder().encode(request.getPassword());
            user.setPassword(encodedPassword);
        }
        if (request.getProfileImage() != null) user.setProfileImage(request.getProfileImage());

        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        return toUserResponse(user);
    }

}

