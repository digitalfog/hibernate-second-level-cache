package com.example.hibernatesecondlevelcache.repository;

import com.example.hibernatesecondlevelcache.entity.pkjoincolumn.MarketPKJoinColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;

public interface MarketPKJoinColumnRepository extends JpaRepository<MarketPKJoinColumn, Long> {

  @QueryHints(value = {@QueryHint(name = "org.hibernate.cacheable", value = "true")})
  MarketPKJoinColumn findByCode(String code);
}
