package com.anhnt.customer.service.mapper;

import com.anhnt.common.domain.customer.request.CreateCustomerRequest;
import com.anhnt.common.domain.customer.request.CustomerMessageCreateRequest;
import com.anhnt.common.domain.customer.request.CustomerMessageCreateResponse;
import com.anhnt.customer.repository.entity.CustomerEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class CustomerMessageMapper {
    public abstract CustomerMessageCreateResponse toCustomerMessageCreateResponse(CustomerMessageCreateRequest request);
}
