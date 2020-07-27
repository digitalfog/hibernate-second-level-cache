package com.example.hibernatesecondlevelcache.repository;

import com.example.hibernatesecondlevelcache.entity.regularjoin.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;

public interface StoreRepository extends JpaRepository<Store, Long> {

  @QueryHints(value = {@QueryHint(name = "org.hibernate.cacheable", value = "true")})
  Store findByCode(String code);
}
