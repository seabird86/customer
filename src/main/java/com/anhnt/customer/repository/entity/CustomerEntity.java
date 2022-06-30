package com.anhnt.customer.repository.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetTime;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import com.anhnt.common.domain.customer.constant.CustomerStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
  private String username;
  private String name;
  private String mobile;
  private LocalDate dateOfBirth;
  private Instant createdDatetime = Instant.now();
  private Instant updatedDatetime = Instant.now();
  private Instant verifiedDatetime;
  private OffsetTime availableTime;
  private BigDecimal balance;
  
  @Enumerated(EnumType.STRING)
  private CustomerStatus status = CustomerStatus.RESERVED;
  @Version
  private Long version;
}
