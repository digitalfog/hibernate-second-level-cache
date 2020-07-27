package com.example.hibernatesecondlevelcache.entity.regularjoin;

import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"market"})
@EqualsAndHashCode(of = "code")
@Entity
@Cacheable
public class Warehouse {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String code;

  @Column private String description;

  @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
  @OneToOne(mappedBy = "warehouse", fetch = FetchType.LAZY, optional = false)
  private Market market;
}
