package com.example.api.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsersRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;
}
