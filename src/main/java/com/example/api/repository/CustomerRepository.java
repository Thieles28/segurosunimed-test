package com.example.api.repository;

import com.example.api.domain.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
	List<Customer> findAllByOrderByNameAsc();
}
