package com.example.hibernatesecondlevelcache.entity.onetoone.workaround;

import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"marketOneToOne"})
@EqualsAndHashCode(of = "code")
@Entity
@Cacheable
public class WarehouseOneToOne {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String code;

  @Column private String description;

  @Getter(AccessLevel.PRIVATE)
  // !!! @Cache annotation turns ON cache of collections
  @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
  @OneToMany(mappedBy = "warehouseOneToOne", fetch = FetchType.LAZY)
  private List<MarketOneToOne> marketOneToOne;

  // WORKAROUND
  // Map OneToOne relation as OneToMany and get first element.
  // In this case - secondLevel cache works.
  public MarketOneToOne getMarket() {
    return marketOneToOne.iterator().next();
  }
}
