package com.anhnt.customer.controller;

import com.anhnt.common.domain.customer.request.CustomerCreateRequest;
import com.anhnt.common.domain.customer.request.CustomerUpdateRequest;
import com.anhnt.common.domain.customer.response.CustomerCreateResponse;
import com.anhnt.common.domain.response.BodyEntity;
import com.anhnt.common.domain.response.ResponseFactory;
import com.anhnt.customer.config.annotation.LogAround;
import com.anhnt.customer.repository.CustomerRepository;
import com.anhnt.customer.repository.entity.CustomerEntity;
import com.anhnt.customer.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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

  @Operation(summary = "Create Customer", responses = {
          @ApiResponse(responseCode = "404", description = "Not found", content = @Content(examples = {@ExampleObject(value="hello",description = "description")})),
          @ApiResponse(responseCode = "401", description = "Authentication Failure", content = @Content(schema = @Schema(hidden = true))) })
  @LogAround(message = "Create customer")
  @PostMapping(value = "/customers")
  public ResponseEntity<BodyEntity<CustomerCreateResponse>> createCustomer(@RequestBody CustomerCreateRequest request) {
    CustomerEntity entity = customerService.createCustomer(request);
    return ResponseFactory.success(new CustomerCreateResponse(entity.getId()));
  }

  @PutMapping(value = "/customers/{id}")
  @LogAround(message = "Update customer")
  public void updateCustomer(@PathVariable Long id, @RequestBody CustomerUpdateRequest request) {
    CustomerEntity entity = customerRepository.findById(id).get();
    customerService.updateCustomer(request,entity);
  }

}
