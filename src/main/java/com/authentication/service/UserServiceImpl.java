package com.authentication.service;

import com.authentication.dto.UserLoginRequest;
import com.authentication.dto.UserRegistrationRequest;
import com.authentication.dto.UserResponse;
import com.authentication.entity.User;
import com.authentication.exception.UserAlreadyExistsException;
import com.authentication.exception.UserNotFoundException;
import com.authentication.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse registerUser(UserRegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email is already registered.");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(PasswordUtil.hashPassword(request.getPassword()));

        User savedUser = userRepository.save(user);

        return toUserResponse(savedUser);
    }



    @Override
    public UserResponse loginUser(UserLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("Invalid email or password."));

        if (!PasswordUtil.verifyPassword(request.getPassword(), user.getPassword())) {
            throw new UserNotFoundException("Invalid email or password.");
        }

        user.setLastLogin(java.time.LocalDateTime.now());
        userRepository.save(user);

        return toUserResponse(user);
    }

    private UserResponse toUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setUserId(user.getUserId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        return response;
    }
}

