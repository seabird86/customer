package com.anhnt.customer.service.mapper;

import com.anhnt.common.domain.customer.request.CreateCustomerRequest;
import com.anhnt.customer.repository.entity.CustomerEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class CustomerMapper {
    public abstract CustomerEntity toCustomerEntity(CreateCustomerRequest request);
}
