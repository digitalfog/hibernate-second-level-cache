package com.example.hibernatesecondlevelcache.repository;

import com.example.hibernatesecondlevelcache.entity.onetoone.workaround.MarketOneToOne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;

public interface MarketOneToOneRepository extends JpaRepository<MarketOneToOne, Long> {

  @QueryHints(value = {@QueryHint(name = "org.hibernate.cacheable", value = "true")})
  MarketOneToOne findByCode(String code);
}
