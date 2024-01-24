package com.example.api.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AddressRequest {
    @NotBlank(message = "O campo 'zipCode' não pode estar em branco")
    @NotNull(message = "O campo 'zipCode' não pode ser nulo")
    private String zipCode;
}
