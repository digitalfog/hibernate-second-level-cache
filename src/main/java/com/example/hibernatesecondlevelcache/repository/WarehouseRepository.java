package com.example.hibernatesecondlevelcache.repository;

import com.example.hibernatesecondlevelcache.entity.regularjoin.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

  @QueryHints(value = {@QueryHint(name = "org.hibernate.cacheable", value = "true")})
  Warehouse findByCode(String code);

  @QueryHints(value = {@QueryHint(name = "org.hibernate.cacheable", value = "true")})
  @Query("Select w from Warehouse w join fetch w.market")
  Warehouse findByCode2(String code);
}
