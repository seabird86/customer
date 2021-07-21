package com.anhnt.customer.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.math.BigDecimal;

@Entity
@Table(name="customer")
@Access(AccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
public class CustomerEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private BigDecimal creditLimit;

//  @Embedded
//  private Money creditLimit;
//
//  @ElementCollection
//  private Map<Long, Money> creditReservations;

  private Long creationTime;

  @Version
  private Long version;

//  Money availableCredit() {
//    return creditLimit.subtract(creditReservations.values().stream().reduce(Money.ZERO, Money::add));
//  }
//
//  public CustomerEntity(String name, Money creditLimit) {
//    this.name = name;
//    this.creditLimit = creditLimit;
//    this.creditReservations = Collections.emptyMap();
//    this.creationTime = System.currentTimeMillis();
//  }
//
//  public static ResultWithEvents<Customer> create(String name, Money creditLimit) {
//    Customer customer = new Customer(name, creditLimit);
//    return new ResultWithEvents<>(customer,
//            singletonList(new CustomerCreatedEvent(customer.getName(), customer.getCreditLimit())));
//  }
//
//  public void reserveCredit(Long orderId, Money orderTotal) {
//    if (availableCredit().isGreaterThanOrEqual(orderTotal)) {
//      creditReservations.put(orderId, orderTotal);
//    } else
//      throw new CustomerCreditLimitExceededException();
//  }
//
//  public void unreserveCredit(long orderId) {
//    creditReservations.remove(orderId);
//  }
}
