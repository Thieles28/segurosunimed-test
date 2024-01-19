package com.example.api.model.request;

import lombok.Data;

@Data
public class CustomerRequest {
    private String name;
    private String email;
    private String gender;
}
