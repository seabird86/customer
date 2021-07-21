package com.anhnt.customer.consumer;

import com.anhnt.common.domain.payment.event.OrderCreatedEvent;
import com.anhnt.customer.service.CustomerService;
import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class OrderEventConsumer {
  private CustomerService customerService;
  public DomainEventHandlers domainEventHandlers() {
    return DomainEventHandlersBuilder
            .forAggregateType("com.anhnt.payment.repository.entity.OrderEntity")
            .onEvent(OrderCreatedEvent.class, this::handleOrderCreatedEvent)
//            .onEvent(OrderCancelledEvent.class, this::handleOrderCancelledEvent)
            .build();
  }

  public void handleOrderCreatedEvent(DomainEventEnvelope<OrderCreatedEvent> domainEventEnvelope) {
    OrderCreatedEvent event = domainEventEnvelope.getEvent();
    customerService.checkCredit(event);


//    customerService.reserveCredit(Long.parseLong(domainEventEnvelope.getAggregateId()),
//            event.getOrderDetails().getCustomerId(), event.getOrderDetails().getOrderTotal());
  }

//  public void handleOrderCancelledEvent(DomainEventEnvelope<OrderCancelledEvent> domainEventEnvelope) {
//    customerService.releaseCredit(Long.parseLong(domainEventEnvelope.getAggregateId()), domainEventEnvelope.getEvent().getOrderDetails().getCustomerId());
//  }

}
