package com.example.hibernatesecondlevelcache.entity.pkjoincolumn;

import lombok.*;

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
public class WarehousePKJoinColumn {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String code;

  @Column private String description;

  @OneToOne(fetch = FetchType.LAZY)
  @PrimaryKeyJoinColumn
  private MarketPKJoinColumn market;
}
