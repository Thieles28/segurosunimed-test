package com.example.api.model.request;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CustomerRequest {
    @NotBlank(message = "O campo 'name' não pode estar em branco")
    @NotNull(message = "O campo 'name' não pode ser nulo")
    private String name;

    @NotBlank(message = "O campo 'email' não pode estar em branco")
    @NotNull(message = "O campo 'email' não pode ser nulo")
    private String email;

    @NotBlank(message = "O campo 'gender' não pode estar em branco")
    @NotNull(message = "O campo 'gender' não pode ser nulo")
    private String gender;

    @Valid
    @NotEmpty(message = "A lista de 'addresses' não pode estar vazia")
    @NotNull(message = "A lista de 'addresses' não pode estar nulo")
    private List<AddressRequest> addresses;
}
