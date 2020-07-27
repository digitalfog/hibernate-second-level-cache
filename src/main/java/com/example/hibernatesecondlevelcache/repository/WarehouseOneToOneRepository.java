package com.example.hibernatesecondlevelcache.repository;

import com.example.hibernatesecondlevelcache.entity.onetoone.workaround.WarehouseOneToOne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;

public interface WarehouseOneToOneRepository extends JpaRepository<WarehouseOneToOne, Long> {

  @QueryHints(value = {@QueryHint(name = "org.hibernate.cacheable", value = "true")})
  WarehouseOneToOne findByCode(String code);
}
