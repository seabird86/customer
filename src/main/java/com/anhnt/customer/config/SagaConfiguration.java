package com.anhnt.customer.config;

import com.anhnt.customer.consumer.OrderEventConsumer;
import com.anhnt.customer.repository.CustomerRepository;
import com.anhnt.customer.repository.entity.CustomerEntity;
import com.anhnt.common.domain.customer.event.CustomerSnapshotEvent;
import io.eventuate.tram.events.common.DomainEvent;
import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import io.eventuate.tram.spring.optimisticlocking.OptimisticLockingDecoratorConfiguration;
import io.eventuate.tram.viewsupport.rebuild.DBLockService;
import io.eventuate.tram.viewsupport.rebuild.DomainEventWithEntityId;
import io.eventuate.tram.viewsupport.rebuild.DomainSnapshotExportService;
import io.eventuate.tram.viewsupport.rebuild.DomainSnapshotExportServiceFactory;
import io.eventuate.tram.viewsupport.rebuild.SnapshotConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories
@EnableAutoConfiguration
@Import({OptimisticLockingDecoratorConfiguration.class,
        SnapshotConfiguration.class})
public class SagaConfiguration {

  @Bean
  public DomainEventDispatcher domainEventDispatcher(OrderEventConsumer consumer, DomainEventDispatcherFactory domainEventDispatcherFactory) {
    return domainEventDispatcherFactory.make("customerService", consumer.domainEventHandlers());
  }

  @Bean
  public DomainSnapshotExportService<CustomerEntity> domainSnapshotExportService(CustomerRepository customerRepository,
                                                                              DomainSnapshotExportServiceFactory<CustomerEntity> domainSnapshotExportServiceFactory) {
    return domainSnapshotExportServiceFactory.make(
            CustomerEntity.class,
            customerRepository,
            customer -> {
              DomainEvent domainEvent = new CustomerSnapshotEvent(customer.getId(),
                      customer.getName(),customer.getCreditLimit());

              return new DomainEventWithEntityId(customer.getId(), domainEvent);
            },
            new DBLockService.TableSpec("customer", "customer0_"),
            "MySqlReader");
  }
}
