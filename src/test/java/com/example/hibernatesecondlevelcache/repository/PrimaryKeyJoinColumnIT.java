package com.example.hibernatesecondlevelcache.repository;

import com.example.hibernatesecondlevelcache.entity.pkjoincolumn.MarketPKJoinColumn;
import com.example.hibernatesecondlevelcache.entity.pkjoincolumn.WarehousePKJoinColumn;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import net.ttddyy.dsproxy.QueryCountHolder;
import org.hibernate.Cache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;

@DBRider
@SpringBootTest
@DataSet(value = "datasets/PrimaryKeyJoinColumnIT/primarykey-join-column.yml")
class PrimaryKeyJoinColumnIT extends AbstractTestContainersIT {

  @Autowired private WarehousePKJoinColumnRepository warehousePKJoinColumnRepository;
  @Autowired private MarketPKJoinColumnRepository marketPKJoinColumnRepository;

  @Autowired private TransactionTemplate transactionTemplate;

  @PersistenceContext protected EntityManager entityManager;

  @BeforeEach
  public void initDB() {
    QueryCountHolder.clear();
    Cache cache = entityManager.getEntityManagerFactory().getCache().unwrap(Cache.class);
    cache.evictAllRegions();
  }

  @Test
  void getWarehouse_and_getOneToOneRelation_PKJoinColumn_with2ndLevelCache_WORKS() {
    System.out.println(">>>> 1st transaction:");

    TransactionCallback<Object> doInTransaction =
        res -> {
          WarehousePKJoinColumn warehouseA =
              warehousePKJoinColumnRepository.findByCode("warehousePKJoinColumn_A");
          System.out.println(warehouseA);
          System.out.println(warehouseA.getMarket());
          return null;
        };

    transactionTemplate.execute(doInTransaction);
    assertThat(QueryCountHolder.getGrandTotal().getSelect()).isEqualTo(2);

    QueryCountHolder.clear();

    System.out.println(">>>> 2nd transaction:");
    transactionTemplate.execute(doInTransaction);
    assertThat(QueryCountHolder.getGrandTotal().getSelect()).isZero();
  }

  @Test
  void getMarket_and_getOneToOneRelation_PKJoinColumn_with2ndLevelCache_WORKS() {
    System.out.println(">>>> 1st transaction:");

    TransactionCallback<Object> doInTransaction =
        res -> {
          MarketPKJoinColumn marketPKJoinColumnA =
              marketPKJoinColumnRepository.findByCode("marketPKJoinColumn_A");
          System.out.println(marketPKJoinColumnA);
          System.out.println(marketPKJoinColumnA.getWarehousePKJoinColumn());
          return null;
        };

    transactionTemplate.execute(doInTransaction);
    assertThat(QueryCountHolder.getGrandTotal().getSelect()).isEqualTo(2);

    QueryCountHolder.clear();

    System.out.println(">>>> 2nd transaction:");
    transactionTemplate.execute(doInTransaction);
    assertThat(QueryCountHolder.getGrandTotal().getSelect()).isZero();
  }
}
