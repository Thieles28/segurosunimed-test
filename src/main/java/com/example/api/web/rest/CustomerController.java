package com.example.api.web.rest;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
	public  ResponseEntity<List<CustomerResponse>> findAll() {
		return ResponseEntity.ok().body(service.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<CustomerResponse> findById(@PathVariable Long id) {
		return service.findById(id)
				.map(customerResponse -> ResponseEntity.ok().body(customerResponse))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer não encontrado"));
	}

	@PostMapping
	public ResponseEntity<CustomerResponse> registerCustomer(@RequestBody CustomerRequest customerRequest){
		return  ResponseEntity.status(HttpStatus.CREATED).body(service.registerCustomer(customerRequest));
	}

	@PutMapping("{id}")
	public ResponseEntity<Optional<CustomerResponse>> updateCustomer(@RequestBody CustomerRequest customerRequest, @PathVariable("id") Long id) {
		return  ResponseEntity.status(HttpStatus.CREATED).body(service.updateCustomer(customerRequest, id));
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Void> removerLivro(@PathVariable Long id) {
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
			@ModelAttribute CustomerRequest customerRequest) {
		return service.searchCustomers(customerRequest);
	}
}
