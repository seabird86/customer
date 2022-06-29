package com.anhnt.customer.service;

import com.anhnt.common.domain.customer.constant.CustomerStatus;
import com.anhnt.common.domain.customer.request.CreateCustomerRequest;
import com.anhnt.common.domain.customer.request.UpdateCustomerRequest;
import com.anhnt.common.domain.payment.request.TransactionCreateParam;
import com.anhnt.common.domain.payment.request.TransactionCreateRequest;
import com.anhnt.common.domain.payment.response.ConfirmCreateTransactionResponse;
import com.anhnt.common.domain.payment.response.TryCreateTransactionResponse;
import com.anhnt.common.domain.response.BodyEntity;
import com.anhnt.customer.client.PaymentClient;
import com.anhnt.customer.repository.CustomerRepository;
import com.anhnt.customer.repository.entity.CustomerEntity;
import com.anhnt.customer.service.mapper.CustomerMapper;
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
  private PaymentClient paymentClient;



  public CustomerEntity createCustomer(CustomerEntity entity) {
    entity = saveCustomer(entity);
    TransactionCreateRequest txn = new TransactionCreateRequest();
    txn.setPayeeId(entity.getId());
    txn.setPayerId(entity.getId());
    txn.setAmount(entity.getBalance());
    TransactionCreateParam param = new TransactionCreateParam();
    param.setServiceId(123);
    param.setDescription("description value");
    BodyEntity<TryCreateTransactionResponse> response = paymentClient.tryCreateTransaction(param, txn);
    entity.setStatus(CustomerStatus.CREATED);
    updateCustomer(entity);
    paymentClient.confirmCreateTransaction(response.getData().getId());
    log.info("Txn ID",response.getData().getId());
    return entity;
  }

  public void tryCreateCustomer(){
    // store in Tcc_coordinator
    // save object with time
    // save with unique id per transaction. here is user id
    // 
  }

  private CustomerEntity saveCustomer(CustomerEntity entity) {
    return customerRepository.save(entity);
  }

  private CustomerEntity updateCustomer(CustomerEntity entity) {
    return customerRepository.save(entity);
  }

  public CustomerEntity updateCustomer(UpdateCustomerRequest request, CustomerEntity entity) {
    BeanUtils.copyProperties(request,entity);
    entity.setUpdatedDatetime(Instant.now());
    return customerRepository.save(entity);
  }
}
