package com.example.api.service;

import com.example.api.model.request.CustomerRequest;
import com.example.api.model.response.CustomerResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
  List<CustomerResponse> findAll();
  Optional<CustomerResponse> findById(Long id);
  CustomerResponse registerCustomer(@RequestBody CustomerRequest customerRequest);
  Optional<CustomerResponse> updateCustomer(@RequestBody CustomerRequest customerRequest, @PathVariable("id") Long id);
  void removeCustomer(@RequestBody CustomerResponse customerResponse);
}
