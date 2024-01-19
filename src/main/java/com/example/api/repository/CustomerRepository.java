package com.example.api.repository;

import com.example.api.domain.Customer;
import com.example.api.model.request.CustomerRequest;
import com.example.api.query.CustomerQueries;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
	List<Customer> findAllByOrderByNameAsc();
	@Query(CustomerQueries.FILTER_CUSTOMER_REQUEST)
	List<Customer> findBy(@Param("customerRequest") CustomerRequest customerRequest);

}
