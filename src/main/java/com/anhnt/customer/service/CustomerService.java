package com.anhnt.customer.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.anhnt.common.domain.constant.TccConstant.CoordinateStatus;
import com.anhnt.common.domain.constant.TccConstant.FunctionName;
import com.anhnt.common.domain.constant.TccConstant.ParticipantId;
import com.anhnt.common.domain.constant.TccConstant.ParticipantStatus;
import com.anhnt.common.domain.customer.constant.CustomerStatus;
import com.anhnt.common.domain.customer.request.UpdateCustomerRequest;
import com.anhnt.common.domain.exception.InvalidRequestException;
import com.anhnt.common.domain.payment.request.TransactionCreateParam;
import com.anhnt.common.domain.payment.request.TransactionCreateRequest;
import com.anhnt.common.domain.response.ErrorFactory;
import com.anhnt.customer.client.PaymentClient;
import com.anhnt.customer.repository.CustomerRepository;
import com.anhnt.customer.repository.TccCoordinatorRepository;
import com.anhnt.customer.repository.TccParticipantRepository;
import com.anhnt.customer.repository.entity.CustomerEntity;
import com.anhnt.customer.repository.entity.TccCoordinatorEntity;
import com.anhnt.customer.repository.entity.TccParticipantEntity;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerService {

  private CustomerRepository customerRepository;
  private TccCoordinatorRepository tccCoordinatorRepository;
  private TccParticipantRepository tccParticipantRepository;
  private PaymentClient paymentClient;



  public CustomerEntity createCustomerByTcc(CustomerEntity entity){
    TccCoordinatorEntity coordinatorEntity = createCoordinatorCreateCustomer(entity);
    // Phase 1: Try
    try{
      entity = tryCreateCustomer(coordinatorEntity,entity);
      tryFirstTransaction(coordinatorEntity,entity);
    }catch(Exception ex){
      // Phase 3: Cancel
      cancelCreateCustomer(coordinatorEntity);
      throw ex;
    }
    // Phase 2: Confirm. Coordinator will either retry confirmation until success or involve manual intervention
    coordinatorEntity.setStatus(CoordinateStatus.CONFIRMING);
    tccCoordinatorRepository.save(coordinatorEntity);
    confirmCreateCustomer(coordinatorEntity.getParticipants().get(0));
    confirmFirstTransaction(coordinatorEntity.getParticipants().get(1));
    // update status of distributed transaction
    coordinatorEntity.setStatus(CoordinateStatus.CONFIRMED);
    tccCoordinatorRepository.save(coordinatorEntity);
    return entity;
  }

  @Transactional
  public TccCoordinatorEntity createCoordinatorCreateCustomer(CustomerEntity entity){
    TccCoordinatorEntity tccEntity = new TccCoordinatorEntity();
    tccEntity.setFunctionName(FunctionName.CREATE_CUSTOMER);
    tccEntity.setExpiryDatetime(entity.getCreatedDatetime().plusSeconds(120));    
    tccEntity = tccCoordinatorRepository.save(tccEntity);
    return tccEntity;
  }

  @Transactional
  public CustomerEntity tryCreateCustomer(TccCoordinatorEntity tccEntity,CustomerEntity entity){
    // Init this participant
    TccParticipantEntity participant = new TccParticipantEntity(tccEntity,ParticipantId.CUSTOMER_SERVICE);
    tccEntity.getParticipants().add(participant);
    tccParticipantRepository.save(participant);

    // try to reserve
    boolean exist = customerRepository.existsByUsernameAndStatusNotIn(entity.getUsername(), List.of(CustomerStatus.DELETED));
    if (exist){
      throw new InvalidRequestException(ErrorFactory.CustomerError.USERNAME_EXISTS.apply(List.of(entity.getUsername())));
    }
    entity.setStatus(CustomerStatus.RESERVED);
    entity = trySaveCustomer(entity);

    // Update participant status
    participant.setStatus(ParticipantStatus.RESERVED);
    participant.setParamId(entity.getId().toString());
    tccParticipantRepository.save(participant);
    return entity;
  }

  public void tryFirstTransaction(TccCoordinatorEntity tccEntity,CustomerEntity entity){
    // Init this participant
    TccParticipantEntity participant = new TccParticipantEntity(tccEntity,ParticipantId.PAYMENT_SERVICE);
    participant.setParamId(entity.getId().toString());
    tccEntity.getParticipants().add(participant);
    tccParticipantRepository.save(participant);

    // try to reserve
    tccParticipantRepository.save(participant);
    TransactionCreateRequest txn = new TransactionCreateRequest();
    txn.setPayeeId(entity.getId());
    txn.setPayerId(entity.getId());
    txn.setAmount(entity.getBalance());
    TransactionCreateParam param = new TransactionCreateParam();
    param.setServiceId(123);
    paymentClient.tryFirstTransaction(entity.getId(), param, txn);

    // Update participant status
    participant.setStatus(ParticipantStatus.RESERVED);
    tccParticipantRepository.save(participant);
  }

  @Transactional
  public void confirmCreateCustomer(TccParticipantEntity participant){
      CustomerEntity entity = customerRepository.findById(Long.valueOf(participant.getParamId())).get();
      entity.setStatus(CustomerStatus.ACTIVE);
      customerRepository.save(entity);

      // Update participant status
      participant.setStatus(ParticipantStatus.CONFIRMED);
      tccParticipantRepository.save(participant);
    
  }

  public void confirmFirstTransaction(TccParticipantEntity participant){
    paymentClient.confirmFirstTransaction(Long.valueOf(participant.getParamId()));

    // Update participant status
    participant.setStatus(ParticipantStatus.CONFIRMED);
    tccParticipantRepository.save(participant);
  }

  public void cancelCreateCustomer(TccCoordinatorEntity entity){
    entity.setStatus(CoordinateStatus.CANCELING);
    tccCoordinatorRepository.save(entity);
    for (TccParticipantEntity participant : entity.getParticipants()){
      if (participant.getStatus().equals(ParticipantStatus.CANCELED)){
        continue;
      }
      if (participant.getStatus().equals(ParticipantStatus.CONFIRMED)){
        throw new RuntimeException("Can't cancel for confirmed participant");
      }
      if (participant.getParticipantId().equals(ParticipantId.CUSTOMER_SERVICE)){
        if (participant.getParamId()!=null){
          Optional<CustomerEntity> option = customerRepository.findById(Long.valueOf(participant.getParamId()));
          if (option.isPresent()){
            CustomerEntity customerEntity = option.get();
            customerEntity.setStatus(CustomerStatus.DELETED);
            customerRepository.save(customerEntity);
          }
        }
      }
      if (participant.getParticipantId().equals(ParticipantId.PAYMENT_SERVICE)){
        paymentClient.cancelFirstTransaction(Long.valueOf(participant.getParamId()));  
      }
      // Update participant status
      participant.setStatus(ParticipantStatus.CANCELED);
      tccParticipantRepository.save(participant);
    }
    entity.setStatus(CoordinateStatus.CANCELED);
    tccCoordinatorRepository.save(entity);
  }

  public CustomerEntity trySaveCustomer(CustomerEntity entity){
    return saveCustomer(entity);
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
