package com.anhnt.customer.repository.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Version;

import com.anhnt.common.domain.constant.TccConstant.CoordinateStatus;
import com.anhnt.common.domain.constant.TccConstant.FunctionName;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="tcc_coordinator")
@Access(AccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
public class TccCoordinatorEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Enumerated(EnumType.STRING)
  private FunctionName functionName;
  
  @OneToMany(mappedBy="coordinator",cascade = CascadeType.PERSIST)
  @OrderBy("id")
  private List<TccParticipantEntity> participants = new ArrayList<>();

  @Enumerated(EnumType.STRING)
  private CoordinateStatus status = CoordinateStatus.TRYING;

  private Instant createdDatetime = Instant.now();

  private Instant expiryDatetime;

  @Version
  private Long version;
}
