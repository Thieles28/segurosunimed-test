package com.example.api.service;


import com.example.api.model.request.SignUpRequest;
import com.example.api.model.request.SigninRequest;
import com.example.api.model.request.UsersRequest;
import com.example.api.model.response.JwtAuthenticationResponse;
import com.example.api.model.response.UserLoggedResponse;
import com.example.api.model.response.UsersResponse;

import java.util.List;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SigninRequest request);

    List<UsersResponse> registeredUsers();

    UserLoggedResponse getUserLoggedSystem();

    UserLoggedResponse getUserLoggedInSystem(String email);

    UsersResponse getUser(Long id);

    UsersResponse registerUser(UsersRequest users);

    UsersResponse updateUser(UsersRequest usersRequest, Long id);

    void removeUser(UsersResponse usersResponse);
}
