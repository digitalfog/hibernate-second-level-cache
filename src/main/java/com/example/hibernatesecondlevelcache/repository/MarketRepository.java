package com.example.hibernatesecondlevelcache.repository;

import com.example.hibernatesecondlevelcache.entity.regularjoin.Market;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;

public interface MarketRepository extends JpaRepository<Market, Long> {

  @QueryHints(value = {@QueryHint(name = "org.hibernate.cacheable", value = "true")})
  Market findByCode(String code);
}
