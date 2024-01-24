package com.example.api.model.response;

import com.example.api.model.request.AddressRequest;
import lombok.Data;

import java.util.List;

@Data
public class CustomerResponse {
    private Long id;
    private String name;
    private String email;
    private String gender;
    private List<AddressResponse> addresses;
}
