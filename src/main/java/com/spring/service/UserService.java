package com.spring.service;

import com.spring.dto.UserRegisterRequest;
import com.spring.dto.UserLoginRequest;
import com.spring.dto.UserResponse;

public interface UserService {
    UserResponse register(UserRegisterRequest request);
    UserResponse login(UserLoginRequest request);
}
