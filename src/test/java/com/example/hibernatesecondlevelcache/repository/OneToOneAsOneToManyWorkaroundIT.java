package com.example.hibernatesecondlevelcache.repository;

import com.example.hibernatesecondlevelcache.entity.onetoone.workaround.MarketOneToOne;
import com.example.hibernatesecondlevelcache.entity.onetoone.workaround.WarehouseOneToOne;
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
@DataSet(value = "datasets/OneToOneAsOneToManyWorkaroundIT/onetoone-as-onetomany.yml")
class OneToOneAsOneToManyWorkaroundIT extends AbstractTestContainersIT {

  @Autowired private WarehouseOneToOneRepository warehouseOneToOneRepository;
  @Autowired private MarketOneToOneRepository marketOneToOneRepository;

  @Autowired private TransactionTemplate transactionTemplate;

  @PersistenceContext protected EntityManager entityManager;

  @BeforeEach
  public void initDB() {
    QueryCountHolder.clear();
    Cache cache = entityManager.getEntityManagerFactory().getCache().unwrap(Cache.class);
    cache.evictAllRegions();
  }

  @Test
  void reverseRelation_getWarehouse_and_getOneToManyMarketRelation_with_2ndLevelCache_WORKS() {
    TransactionCallback<Object> doInTransaction =
        res -> {
          WarehouseOneToOne warehouseOneToOneA = warehouseOneToOneRepository.findByCode("warehouseOneToOne_A");
          System.out.println(warehouseOneToOneA);
          System.out.println(warehouseOneToOneA.getMarket());
          return null;
        };

    System.out.println(">>>> 1st transaction:");
    transactionTemplate.execute(doInTransaction);
    assertThat(QueryCountHolder.getGrandTotal().getSelect()).isEqualTo(2);

    QueryCountHolder.clear();

    System.out.println(">>>> 2nd transaction:");
    transactionTemplate.execute(doInTransaction);
    assertThat(QueryCountHolder.getGrandTotal().getSelect()).isZero();

    Cache cache = entityManager.getEntityManagerFactory().getCache().unwrap(Cache.class);
    assertThat(cache.containsEntity(WarehouseOneToOne.class, 1000L)).isTrue();
    assertThat(cache.containsEntity(MarketOneToOne.class, 1200L)).isTrue();
  }

  @Test
  void getMarket_and_getManyToOneWarehouseRelation_with_2ndLevelCache_WORKS() {
    TransactionCallback<Object> doInTransaction =
        res -> {
          MarketOneToOne marketOneToOneA = marketOneToOneRepository.findByCode("marketOneToOne_A");
          System.out.println(marketOneToOneA);
          System.out.println(marketOneToOneA.getWarehouseOneToOne());
          return null;
        };

    System.out.println(">>>> 1st transaction:");
    transactionTemplate.execute(doInTransaction);
    assertThat(QueryCountHolder.getGrandTotal().getSelect()).isEqualTo(2);

    QueryCountHolder.clear();

    System.out.println(">>>> 2nd transaction:");
    transactionTemplate.execute(doInTransaction);
    assertThat(QueryCountHolder.getGrandTotal().getSelect()).isZero();

    Cache cache = entityManager.getEntityManagerFactory().getCache().unwrap(Cache.class);
    assertThat(cache.containsEntity(WarehouseOneToOne.class, 1000L)).isTrue();
    assertThat(cache.containsEntity(MarketOneToOne.class, 1200L)).isTrue();
  }
}
