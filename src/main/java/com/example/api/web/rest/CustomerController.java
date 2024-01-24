package com.example.api.web.rest;

import com.example.api.model.request.AddressRequest;
import com.example.api.model.request.CustomerFilterRequest;
import com.example.api.model.request.CustomerRequest;
import com.example.api.model.response.CustomerResponse;
import com.example.api.service.impl.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerController {

	private CustomerServiceImpl service;

	@Autowired
	public CustomerController(CustomerServiceImpl service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<List<CustomerResponse>> findAll(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		return ResponseEntity.ok().body(service.findAll(page, size));
	}

	@GetMapping("/{id}")
	public ResponseEntity<CustomerResponse> findById(@PathVariable Long id) {
		return service.findById(id)
				.map(customerResponse -> ResponseEntity.ok().body(customerResponse))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer não encontrado"));
	}

	@PostMapping
	public ResponseEntity<CustomerResponse> registerCustomer(@Valid @RequestBody CustomerRequest customerRequest){
		return  ResponseEntity.status(HttpStatus.CREATED).body(service.registerCustomer(customerRequest));
	}

	@PutMapping("{id}")
	public ResponseEntity<Optional<CustomerResponse>> updateCustomer(@Valid @RequestBody CustomerRequest customerRequest, @PathVariable("id") Long id) {
		return  ResponseEntity.status(HttpStatus.CREATED).body(service.updateCustomer(customerRequest, id));
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Void> removerCustomer(@PathVariable Long id) {
		Optional<CustomerResponse> customerResponseOptional = service.findById(id);

		if (customerResponseOptional.isPresent()) {
			service.removeCustomer(customerResponseOptional.get());
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@GetMapping("search")
	public List<CustomerResponse> searchCustomers(
			@ModelAttribute CustomerFilterRequest filterRequest) {
		return service.searchCustomers(filterRequest);
	}

//	@GetMapping("/{cep}/address")
//	public ResponseEntity<AddressRequest> findById(@PathVariable String cep) {
//		AddressRequest addressRequest = service.getAddressByCEP(cep);
//		return ResponseEntity.ok().body(addressRequest);
//	}
}
