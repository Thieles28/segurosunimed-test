package com.example.api.query;

import org.springframework.stereotype.Component;

@Component
public class CustomerQueries {
  public static final String FILTER_CUSTOMER_REQUEST =
      "SELECT c FROM Customer c " +
          "WHERE (:#{#customerRequest.name} IS NULL OR c.name LIKE %:#{#customerRequest.name}%) " +
          "AND (:#{#customerRequest.email} IS NULL OR c.email LIKE %:#{#customerRequest.email}%) " +
          "AND (:#{#customerRequest.gender} IS NULL OR c.gender LIKE %:#{#customerRequest.gender}%) " +
          "ORDER BY c.name ASC";
}
