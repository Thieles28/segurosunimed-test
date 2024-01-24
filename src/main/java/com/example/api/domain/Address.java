package com.example.api.domain;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Data
@Entity
@Table(name = "ADDRESS")
public class Address {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  @NotEmpty
  private String street;
  @Column(nullable = false)
  @NotEmpty
  private String city;
  @Column(nullable = false)
  @NotEmpty
  private String state;
  @Column(nullable = false)
  @NotEmpty
  private String zipCode;

  @ManyToOne
  @JoinColumn(name = "customer_id", nullable = false)
  private Customer customer;
}
