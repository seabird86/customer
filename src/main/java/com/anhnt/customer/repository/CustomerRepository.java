package com.anhnt.customer.repository;

import com.anhnt.customer.repository.entity.CustomerEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CustomerRepository extends PagingAndSortingRepository<CustomerEntity, Long> {
}
