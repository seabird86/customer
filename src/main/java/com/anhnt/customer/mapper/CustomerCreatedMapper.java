package com.anhnt.customer.mapper;

import com.anhnt.common.domain.customer.event.CustomerCreatedEvent;
import com.anhnt.customer.repository.entity.CustomerEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerCreatedMapper {
    CustomerCreatedEvent toCustomerCreatedEvent(CustomerEntity entity);
}
