package com.example.hibernatesecondlevelcache.entity.onetoone.workaround;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"warehouseOneToOne"})
@EqualsAndHashCode(of = "code")
@Entity
@Cacheable
public class MarketOneToOne {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String code;

  @Column private String description;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "warehouse_one_to_one_id")
  private WarehouseOneToOne warehouseOneToOne;
}
