package com.example.api.service.impl;

import com.example.api.domain.Address;
import com.example.api.domain.Customer;
import com.example.api.model.request.CustomerFilterRequest;
import com.example.api.model.request.CustomerRequest;
import com.example.api.model.response.AddressResponse;
import com.example.api.model.response.CustomerResponse;
import com.example.api.model.response.ViaCEPResponse;
import com.example.api.repository.CustomerRepository;
import com.example.api.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CustomerServiceImpl implements CustomerService {

	private static final String VIA_CEP_URL = "https://viacep.com.br/ws/{cep}/json/";

	@Autowired
	private WebClient.Builder webClientBuilder;

	private final CustomerRepository repository;
	private final ModelMapper modelMapper;

	@Autowired
	public CustomerServiceImpl(CustomerRepository repository, ModelMapper modelMapper) {
		this.repository = repository;
    this.modelMapper = modelMapper;
  }

	public ViaCEPResponse getAddressByCEP(String cep) {
		WebClient webClient = webClientBuilder.build();

		return webClient.get()
				.uri(VIA_CEP_URL, cep)
				.retrieve()
				.bodyToMono(ViaCEPResponse.class)
				.block();
	}

	public List<CustomerResponse> findAll(int page, int size) {
		Page<Customer> customerPage = repository.findAllByOrderByNameAsc(PageRequest.of(page, size));
		return customerPage.getContent().stream()
				.map(customer -> modelMapper.map(customer, CustomerResponse.class))
				.collect(Collectors.toList());
	}

	public Optional<CustomerResponse> findById(Long id) {
		return repository.findById(id)
				.map(customer -> modelMapper.map(customer, CustomerResponse.class));
	}

	@Transactional
	@Override
	public CustomerResponse registerCustomer(CustomerRequest customerRequest) {
		Customer customer = modelMapper.map(customerRequest, Customer.class);

		final Customer finalCustomer = customer;
		List<Address> addresses = customerRequest.getAddresses().stream()
				.map(addressRequest -> {
					if (addressRequest.getZipCode() != null && !addressRequest.getZipCode().isEmpty()) {
						ViaCEPResponse addressByCEP = getAddressByCEP(addressRequest.getZipCode());
						Address address = modelMapper.map(AddressResponse.builder()
								.zipCode(addressByCEP.getCep())
								.street(addressByCEP.getLogradouro())
								.city(addressByCEP.getLocalidade())
								.state(addressByCEP.getUf())
								.build(), Address.class);
						address.setCustomer(finalCustomer);
						return address;
					}
					return null;
				})
				.collect(Collectors.toList());

		customer.setAddresses(addresses);
		customer = repository.save(customer);

		return modelMapper.map(customer, CustomerResponse.class);
	}

	@Override
	public Optional<CustomerResponse> updateCustomer(CustomerRequest customerRequest, Long id) {
		Optional<CustomerResponse> existingCustomer = findById(id);

		existingCustomer.ifPresent(customerResponse -> {
			modelMapper.map(customerRequest, customerResponse);
			Customer customer = modelMapper.map(customerResponse, Customer.class);

			List<Address> updatedAddresses = customerRequest.getAddresses().stream()
					.map(addressRequest -> {
						if (addressRequest.getZipCode() != null && !addressRequest.getZipCode().isEmpty()) {
							ViaCEPResponse addressByCEP = getAddressByCEP(addressRequest.getZipCode());
							Address address = modelMapper.map(AddressResponse.builder()
									.zipCode(addressByCEP.getCep())
									.street(addressByCEP.getLogradouro())
									.city(addressByCEP.getLocalidade())
									.state(addressByCEP.getUf())
									.build(), Address.class);
							address.setCustomer(customer);
							return address;
						}
						return null;
					})
					.collect(Collectors.toList());
			customer.setAddresses(updatedAddresses);
			repository.save(customer);
		});

		return existingCustomer;
	}

	@Override
	public void removeCustomer(CustomerResponse customerResponse) {
		repository.delete(modelMapper.map(customerResponse, Customer.class));
	}

	@Override
	public List<CustomerResponse> searchCustomers(CustomerFilterRequest filterRequest) {
		Iterable<Customer> allCustomers = repository.findAll();

		return StreamSupport.stream(allCustomers.spliterator(), false)
				.filter(customer -> (filterRequest.getName() == null || customer.getName().contains(filterRequest.getName()))
						&& (filterRequest.getEmail() == null || customer.getEmail().contains(filterRequest.getEmail()))
						&& (filterRequest.getGender() == null || customer.getGender().contains(filterRequest.getGender()))
						&& (filterRequest.getCity() == null ||
						customer.getAddresses().stream().anyMatch(address ->
                address.getCity().contains(filterRequest.getCity())
						)))
				.map(customer -> modelMapper.map(customer, CustomerResponse.class))
				.collect(Collectors.toList());
	}
}
