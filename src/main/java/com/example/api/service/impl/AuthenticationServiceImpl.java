package com.example.api.service.impl;

import com.example.api.domain.Role;
import com.example.api.domain.User;
import com.example.api.model.request.SignUpRequest;
import com.example.api.model.request.SigninRequest;
import com.example.api.model.request.UsersRequest;
import com.example.api.model.response.JwtAuthenticationResponse;
import com.example.api.model.response.UserLoggedResponse;
import com.example.api.model.response.UsersResponse;
import com.example.api.repository.UserRepository;
import com.example.api.service.AuthenticationService;
import com.example.api.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public JwtAuthenticationResponse signup(SignUpRequest request) {
        var user = User.builder().firstName(request.getFirstName()).lastName(request.getLastName())
                .email(request.getEmail()).password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER).build();
        userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    @Override
    public JwtAuthenticationResponse signin(SigninRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    @Override
    public List<UsersResponse> registeredUsers() {
        return userRepository.findAll().stream().map(user ->
                modelMapper.map(user, UsersResponse.class)).collect(Collectors.toList());
    }

    @Override
    public UserLoggedResponse getUserLoggedSystem() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return getUserLoggedInSystem(authentication.getName());
    }

    public UserLoggedResponse getUserLoggedInSystem(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário logado não encontrado"));

        List<String> permissions = Arrays.stream(user.getRole().name().split("_"))
                .map(String::toUpperCase)
                .collect(Collectors.toList());

        return UserLoggedResponse.builder()
                .id(user.getId())
                .userLogged(user.getFirstName())
                .permissions(permissions)
                .build();
    }

    @Override
    public UsersResponse getUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
        return modelMapper.map(user, UsersResponse.class);
    }

    @Override
    public UsersResponse registerUser(UsersRequest users) {
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        User user = userRepository.save(modelMapper.map(users, User.class));
        return modelMapper.map(user, UsersResponse.class);
    }

    @Override
    public UsersResponse updateUser(UsersRequest usersRequest, Long id) {
        UsersResponse usersResponse = getUser(id);
        modelMapper.map(usersRequest, usersResponse);

        User user = modelMapper.map(usersResponse, User.class);
        userRepository.save(user);

        return usersResponse;
    }

    @Override
    public void removeUser(UsersResponse usersResponse) {
        User user = modelMapper.map(usersResponse, User.class);
        userRepository.delete(user);
    }

    @PostConstruct
    public void initializeUserData() {
        User user = User.builder()
            .firstName("Thieles")
            .lastName("Martins")
            .email("thieles@gmail.com")
            .password(passwordEncoder.encode("12345678"))
            .role(Role.ADMIN)
            .build();
        userRepository.save(user);
    }
}
