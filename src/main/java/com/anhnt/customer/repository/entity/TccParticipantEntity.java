package com.anhnt.customer.repository.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import com.anhnt.common.domain.constant.TccConstant.ParticipantId;
import com.anhnt.common.domain.constant.TccConstant.ParticipantStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="tcc_participant")
@Access(AccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor

public class TccParticipantEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  public TccParticipantEntity(TccCoordinatorEntity coordinator, ParticipantId participantId){
    this.participantId = participantId;
    this.coordinator = coordinator;
  }

  @ManyToOne()
  @JoinColumn(name="coordinator_id", nullable=false)  
  private TccCoordinatorEntity coordinator;

  @Enumerated(EnumType.STRING)
  private ParticipantId participantId;

  @Enumerated(EnumType.STRING)
  private ParticipantStatus status = ParticipantStatus.INIT;

  private String paramId;

  @Version
  private Long version;
}
