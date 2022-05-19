package com.anhnt.customer.service;

import com.anhnt.customer.controller.request.CustomerCreateRequest;
import com.anhnt.customer.controller.request.CustomerUpdateRequest;
import com.anhnt.customer.mapper.CustomerMapper;
import com.anhnt.customer.repository.CustomerRepository;
import com.anhnt.customer.repository.entity.CustomerEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.Instant;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerService {

  private CustomerRepository customerRepository;
  private CustomerMapper customerMapper;


  @Transactional
  public CustomerEntity createCustomer(CustomerCreateRequest request) {
    CustomerEntity entity = customerMapper.toCustomerEntity(request);
    entity = customerRepository.save(entity);
    return entity;
  }

  public CustomerEntity updateCustomer(CustomerUpdateRequest request, CustomerEntity entity) {
    BeanUtils.copyProperties(request,entity);
    entity.setUpdatedDatetime(Instant.now());
    return customerRepository.save(entity);
  }
}
