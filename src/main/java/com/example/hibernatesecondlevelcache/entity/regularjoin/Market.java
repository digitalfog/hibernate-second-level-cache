package com.example.hibernatesecondlevelcache.entity.regularjoin;

import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"warehouse", "stores"})
@EqualsAndHashCode(of = "code")
@Entity
@Cacheable
public class Market {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String code;

  @Column private String description;

  // Don't use @MapsId if OneToOne tables are connected via separate FK column.
  // Because, real PK will be hidden.
  // For example, Hibernate replaces PK of market (field id) by PK of warehouse.
  // i.e. real market.PK = 300, but Hibernate populates it with market.id = 100 (== warehouse.pk ==
  // market.warehouse_id)
  // @MapsId
  @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "warehouse_id")
  private Warehouse warehouse;

  // !!! @Cache annotation turns ON cache of collections
  @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
  @OneToMany(mappedBy = "market")
  private Set<Store> stores;

}
