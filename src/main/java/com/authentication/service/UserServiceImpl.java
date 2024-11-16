package com.authentication.service;

import com.authentication.config.PasswordEncoder;
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
        user.setPassword(encoded_password);

        User savedUser = userRepository.save(user);
        return toUserResponse(savedUser);
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

