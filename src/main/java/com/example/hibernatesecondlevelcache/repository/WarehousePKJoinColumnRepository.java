package com.example.hibernatesecondlevelcache.repository;

import com.example.hibernatesecondlevelcache.entity.pkjoincolumn.WarehousePKJoinColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;

public interface WarehousePKJoinColumnRepository
    extends JpaRepository<WarehousePKJoinColumn, Long> {

  @QueryHints(value = {@QueryHint(name = "org.hibernate.cacheable", value = "true")})
  WarehousePKJoinColumn findByCode(String code);
}
