package com.anhnt.customer.service;

import com.anhnt.customer.controller.request.CreateCustomerRequest;
import com.anhnt.customer.mapper.CustomerCreatedMapper;
import com.anhnt.customer.repository.CustomerRepository;
import com.anhnt.customer.repository.entity.CustomerEntity;
import com.anhnt.common.domain.payment.event.OrderCreatedEvent;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerService {

  private CustomerRepository customerRepository;
  private DomainEventPublisher domainEventPublisher;
  private CustomerCreatedMapper customerCreatedMapper;


  @Transactional
  public CustomerEntity createCustomer(CreateCustomerRequest createCustomerRequest) {
    CustomerEntity entity = new CustomerEntity();
    entity.setName(createCustomerRequest.getName());
    entity.setCreditLimit(createCustomerRequest.getCreditLimit());
    entity = customerRepository.save(entity);
    domainEventPublisher.publish(CustomerEntity.class, entity.getId(), List.of(customerCreatedMapper.toCustomerCreatedEvent(entity)));
    return entity;
  }

  public CustomerEntity updateCustomer(Long id, CreateCustomerRequest createCustomerRequest) {
    Optional<CustomerEntity> customerOpt = customerRepository.findById(id);
    CustomerEntity entity = customerOpt.get();
    entity.setName(createCustomerRequest.getName());
    entity.setCreditLimit(createCustomerRequest.getCreditLimit());
    entity = customerRepository.save(entity);
    return entity;
  }

  public void checkCredit(OrderCreatedEvent event){
    Optional<CustomerEntity> customerOpt = customerRepository.findById(event.getCustomerId());
    if (customerOpt.isEmpty()) {
      throw new RuntimeException();
    }
      CustomerEntity entity = customerOpt.get();
      entity.setCreditLimit(entity.getCreditLimit().add(new BigDecimal(10)));
      customerRepository.save(entity);
  }
}
