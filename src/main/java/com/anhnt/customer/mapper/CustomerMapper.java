package com.anhnt.customer.mapper;

import com.anhnt.customer.controller.request.CustomerCreateRequest;
import com.anhnt.customer.repository.entity.CustomerEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class CustomerMapper {
    public abstract CustomerEntity toCustomerEntity(CustomerCreateRequest request);
}
