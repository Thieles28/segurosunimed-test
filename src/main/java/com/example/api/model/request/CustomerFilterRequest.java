package com.example.api.model.request;

import lombok.Data;

@Data
public class CustomerFilterRequest {
    private String name;
    private String email;
    private String gender;
    private String city;
    private String state;
}
