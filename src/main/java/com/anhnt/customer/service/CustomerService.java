package com.anhnt.customer.service;

import com.anhnt.common.domain.customer.request.CustomerCreateRequest;
import com.anhnt.common.domain.customer.request.CustomerUpdateRequest;
import com.anhnt.common.domain.payment.request.TransactionCreateRequest;
import com.anhnt.customer.client.PaymentClient;
import com.anhnt.customer.service.mapper.CustomerMapper;
import com.anhnt.customer.repository.CustomerRepository;
import com.anhnt.customer.repository.entity.CustomerEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerService {

  private CustomerRepository customerRepository;
  private CustomerMapper customerMapper;
  private PaymentClient paymentClient;


  @Transactional
  public CustomerEntity createCustomer(CustomerCreateRequest request) {
    CustomerEntity entity = customerMapper.toCustomerEntity(request);
    entity = customerRepository.save(entity);
    TransactionCreateRequest txn = new TransactionCreateRequest();
    txn.setPayeeId(entity.getId());
    txn.setPayerId(entity.getId());
    txn.setAmount(new BigDecimal(10));
    Long value = paymentClient.createTransaction(txn);
    log.info("value",value);
    return entity;
  }

  public CustomerEntity updateCustomer(CustomerUpdateRequest request, CustomerEntity entity) {
    BeanUtils.copyProperties(request,entity);
    entity.setUpdatedDatetime(Instant.now());
    return customerRepository.save(entity);
  }
}
