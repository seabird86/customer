package com.anhnt.customer.controller;

import com.anhnt.common.domain.customer.request.CustomerCreateRequest;
import com.anhnt.common.domain.customer.request.CustomerUpdateRequest;
import com.anhnt.customer.config.annotation.LogAround;
import com.anhnt.customer.repository.CustomerRepository;
import com.anhnt.customer.repository.entity.CustomerEntity;
import com.anhnt.customer.service.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
public class CustomerController {

  private CustomerService customerService;
  private CustomerRepository customerRepository;

  @PostMapping(value = "/customers")
  @LogAround(message = "Create customer")
  public Long createCustomer(@RequestBody CustomerCreateRequest request) {
    CustomerEntity entity = customerService.createCustomer(request);
    return entity.getId();
  }

  @PutMapping(value = "/customers/{id}")
  @LogAround(message = "Update customer")
  public void updateCustomer(@PathVariable Long id, @RequestBody CustomerUpdateRequest request) {
    CustomerEntity entity = customerRepository.findById(id).get();
    customerService.updateCustomer(request,entity);
  }
}
