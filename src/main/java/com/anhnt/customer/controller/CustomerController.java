package com.anhnt.customer.controller;

import com.anhnt.customer.controller.request.CreateCustomerRequest;
import com.anhnt.customer.repository.entity.CustomerEntity;
import com.anhnt.customer.service.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
public class CustomerController {

  private CustomerService customerService;

  @RequestMapping(value = "/customers", method = RequestMethod.POST)
  public Long createCustomer(@RequestBody CreateCustomerRequest createCustomerRequest) {
    CustomerEntity customer = customerService.createCustomer(createCustomerRequest);
    return customer.getId();
  }

  @RequestMapping(value = "/customers/{id}", method = RequestMethod.PUT)
  public Long updateCustomer(@PathVariable Long id, @RequestBody CreateCustomerRequest createCustomerRequest) {
    log.info("Add customer");

    CustomerEntity customer = customerService.updateCustomer(id,createCustomerRequest);
    return customer.getId();
  }

//  @RequestMapping(value = "/customers/make-snapshot", method = RequestMethod.POST)
//  public String makeSnapshot() {
//    return JSonMapper.toJson(domainSnapshotExportService.exportSnapshots());
//  }
}
