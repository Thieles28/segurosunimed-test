package com.example.api.model.response;

import lombok.Data;

@Data
public class CustomerResponse {
    private Long id;
    private String name;
    private String email;
    private String gender;
}
