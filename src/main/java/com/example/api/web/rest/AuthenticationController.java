package com.example.api.web.rest;

import com.example.api.model.request.SignUpRequest;
import com.example.api.model.request.SigninRequest;
import com.example.api.model.request.UsersRequest;
import com.example.api.model.response.JwtAuthenticationResponse;
import com.example.api.model.response.UserLoggedResponse;
import com.example.api.model.response.UsersResponse;
import com.example.api.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @PostMapping("register")
    public ResponseEntity<JwtAuthenticationResponse> signup(@RequestBody SignUpRequest request) {
        return ResponseEntity.ok(authenticationService.signup(request));
    }

    @PostMapping("login")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SigninRequest request) {
        return ResponseEntity.ok(authenticationService.signin(request));
    }

    @GetMapping("users")
    public ResponseEntity<List<UsersResponse>> registeredUsers(){
        return  ResponseEntity.ok().body(authenticationService.registeredUsers());
    }

    @GetMapping("loggedUser")
    public ResponseEntity<UserLoggedResponse> getUserLogged() {
        return ResponseEntity.ok().body(authenticationService.getUserLoggedSystem());
    }

    @PostMapping("registerUser")
    public ResponseEntity<UsersResponse> registerUser(@Valid @RequestBody UsersRequest usersRequest){
        return  ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.registerUser(usersRequest));
    }

    @GetMapping("user/{id}")
    public ResponseEntity<UsersResponse> getUser(@PathVariable("id") Long id){
        return ResponseEntity.ok().body(authenticationService.getUser(id));
    }

    @PutMapping("updateUser/{id}")
    public ResponseEntity<UsersResponse> updateUser(@RequestBody UsersRequest usersRequest, @PathVariable("id") Long id) {
        return  ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.updateUser(usersRequest, id));
    }

    @DeleteMapping("removeUser/{id}")
    public ResponseEntity<UsersResponse> removeUser(@PathVariable Long id){
        UsersResponse usersResponse = authenticationService.getUser(id);
        authenticationService.removeUser(usersResponse);
        return  ResponseEntity.status(HttpStatus.NO_CONTENT).body(usersResponse);
    }

}
