package com.example.api.service.impl;

import com.example.api.domain.Customer;
import com.example.api.model.request.CustomerRequest;
import com.example.api.model.response.CustomerResponse;
import com.example.api.repository.CustomerRepository;
import com.example.api.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

	private final CustomerRepository repository;
	private final ModelMapper modelMapper;

	@Autowired
	public CustomerServiceImpl(CustomerRepository repository, ModelMapper modelMapper) {
		this.repository = repository;
    this.modelMapper = modelMapper;
  }

	public List<CustomerResponse> findAll() {
		return repository.findAllByOrderByNameAsc().stream().map(customer ->
				modelMapper.map(customer, CustomerResponse.class)).collect(Collectors.toList());
	}

	public Optional<CustomerResponse> findById(Long id) {
		return repository.findById(id)
				.map(customer -> modelMapper.map(customer, CustomerResponse.class));
	}

	@Override
	public CustomerResponse registerCustomer(CustomerRequest customerRequest) {
		Customer customer = repository.save(modelMapper.map(customerRequest, Customer.class));
		return modelMapper.map(customer, CustomerResponse.class);
	}

	@Override
	public Optional<CustomerResponse> updateCustomer(CustomerRequest customerRequest, Long id) {
		Optional<CustomerResponse> existingCustomer = findById(id);

		existingCustomer.ifPresent(customerResponse -> {
			modelMapper.map(customerRequest, customerResponse);
			repository.save(modelMapper.map(customerResponse, Customer.class));
		});

		return existingCustomer;
	}

	@Override
	public void removeCustomer(CustomerResponse customerResponse) {
		repository.delete(modelMapper.map(customerResponse, Customer.class));
	}

	@Override
	public List<CustomerResponse> searchCustomers(CustomerRequest customerRequest) {
		return repository
				.findBy(customerRequest).stream()
				.map(customer -> modelMapper.map(customer, CustomerResponse.class))
				.collect(Collectors.toList());
	}
}
