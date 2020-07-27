package com.example.hibernatesecondlevelcache.entity.regularjoin;

import lombok.*;

import javax.persistence.*;
import java.time.ZoneId;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"market"})
@EqualsAndHashCode(of = "code")
@Entity
@Cacheable
public class Store {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String code;

  @Column private String description;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "market_id")
  private Market market;

  @Column(name = "time_zone")
  private String timeZone;

  public ZoneId getZoneId() {
    return ZoneId.of(timeZone);
  }
}
