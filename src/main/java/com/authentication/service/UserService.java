package com.authentication.service;

import com.authentication.dto.UserLoginRequest;
import com.authentication.dto.UserRegistrationRequest;
import com.authentication.dto.UserResponse;

public interface UserService {
    UserResponse registerUser(UserRegistrationRequest request);

    UserResponse loginUser(UserLoginRequest request);
}

