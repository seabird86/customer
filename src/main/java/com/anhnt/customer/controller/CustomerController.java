package com.anhnt.customer.controller;

import com.anhnt.common.domain.customer.request.CreateCustomerRequest;
import com.anhnt.common.domain.customer.request.CustomerMessageCreateRequest;
import com.anhnt.common.domain.customer.request.CustomerMessageCreateResponse;
import com.anhnt.common.domain.customer.request.UpdateCustomerRequest;
import com.anhnt.common.domain.customer.response.CreateCustomerResponse;
import com.anhnt.common.domain.payment.response.TryCreateTransactionResponse;
import com.anhnt.common.domain.response.BodyEntity;
import com.anhnt.common.domain.response.ResponseFactory;
import com.anhnt.common.domain.response.ErrorFactory.CustomerError;
import com.anhnt.customer.config.annotation.LogAround;
import com.anhnt.customer.repository.CustomerRepository;
import com.anhnt.customer.repository.entity.CustomerEntity;
import com.anhnt.customer.service.CustomerService;
import com.anhnt.customer.service.mapper.CustomerMessageMapper;
import com.anhnt.customer.service.mapper.CustomerMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

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
  private CustomerMessageMapper customerMessageMapper;
  private CustomerMapper customerMapper;

  @Operation(summary = "Create Customer", responses = {
          @ApiResponse(responseCode = "404", description = "Not found", content = @Content(examples = {@ExampleObject(value="hello",description = "description")})),
          @ApiResponse(responseCode = "401", description = "Authentication Failure", content = @Content(schema = @Schema(hidden = true))) })
  @LogAround(message = "Create customer")
  @PostMapping(value = "/customers")
  public ResponseEntity<BodyEntity<CreateCustomerResponse>> createCustomer(@RequestBody CreateCustomerRequest request) {
    if (request.getBalance().compareTo(new BigDecimal(0)) < 0){
      return CustomerError.BALANCE_MUST_BE_POSITIVE.apply(null).toResponseEntity();
    }
    CustomerEntity entity = customerMapper.toCustomerEntity(request);
    entity = customerService.createCustomer(entity);
    return ResponseFactory.success(new CreateCustomerResponse(entity.getId()));
  }


  @Operation(summary = "Add messages")
  @LogAround(message = "Add messages")
  @PostMapping(value = "/messages")
  public ResponseEntity<BodyEntity<CustomerMessageCreateResponse>> addMessage(@RequestBody CustomerMessageCreateRequest request) {
    request.setName("Response");
    return ResponseFactory.success(customerMessageMapper.toCustomerMessageCreateResponse(request));
  }

  @PutMapping(value = "/customers/{id}")
  @LogAround(message = "Update customer")
  public void updateCustomer(@PathVariable Long id, @RequestBody UpdateCustomerRequest request) {
    CustomerEntity entity = customerRepository.findById(id).get();
    customerService.updateCustomer(request,entity);
  }

}
