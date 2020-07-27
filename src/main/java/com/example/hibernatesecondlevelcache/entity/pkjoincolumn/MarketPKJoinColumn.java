package com.example.hibernatesecondlevelcache.entity.pkjoincolumn;

import com.example.hibernatesecondlevelcache.entity.regularjoin.Store;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"warehousePKJoinColumn", "stores"})
@EqualsAndHashCode(of = "code")
@Entity
@Cacheable
public class MarketPKJoinColumn {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String code;

  @Column private String description;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "warehousepkjoincolumn_id")
  private WarehousePKJoinColumn warehousePKJoinColumn;

  // !!! @Cache annotation turns ON cache of collections
  @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
  @OneToMany(mappedBy = "market")
  private Set<Store> stores;
}
