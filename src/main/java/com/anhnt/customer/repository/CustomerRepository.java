package com.anhnt.customer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anhnt.common.domain.customer.constant.CustomerStatus;
import com.anhnt.customer.repository.entity.CustomerEntity;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

    boolean existsByUsernameAndStatusNotIn(String username, List<CustomerStatus> status);
}
