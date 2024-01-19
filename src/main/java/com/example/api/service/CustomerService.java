package com.example.api.service;

import com.example.api.model.request.CustomerRequest;
import com.example.api.model.response.CustomerResponse;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
  List<CustomerResponse> findAll(int page, int size);
  Optional<CustomerResponse> findById(Long id);
  CustomerResponse registerCustomer(CustomerRequest customerRequest);
  Optional<CustomerResponse> updateCustomer(CustomerRequest customerRequest, Long id);
  void removeCustomer(CustomerResponse customerResponse);
  List<CustomerResponse> searchCustomers(CustomerRequest customerRequest);
}
