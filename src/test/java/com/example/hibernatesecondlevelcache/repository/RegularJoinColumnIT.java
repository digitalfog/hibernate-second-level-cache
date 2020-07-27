package com.example.hibernatesecondlevelcache.repository;

import com.example.hibernatesecondlevelcache.entity.regularjoin.Market;
import com.example.hibernatesecondlevelcache.entity.regularjoin.Store;
import com.example.hibernatesecondlevelcache.entity.regularjoin.Warehouse;
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
@DataSet(value = "datasets/RegularJoinColumnIT/regular-join-column.yml")
class RegularJoinColumnIT extends AbstractTestContainersIT {

  @Autowired private StoreRepository storeRepository;
  @Autowired private WarehouseRepository warehouseRepository;
  @Autowired private MarketRepository marketRepository;

  @Autowired private TransactionTemplate transactionTemplate;

  @PersistenceContext protected EntityManager entityManager;

  @BeforeEach
  public void initDB() {
    QueryCountHolder.clear();
    Cache cache = entityManager.getEntityManagerFactory().getCache().unwrap(Cache.class);
    cache.evictAllRegions();
  }

  @Test
  void getStore_and_getManyToOneRelation_with_2ndLevelCache_WORKS() {
    System.out.println("USES QUERY CACHE");

    System.out.println(">>>> 1st transaction:");
    TransactionCallback<Object> doInTransaction = res -> {
      Store storeA = storeRepository.findByCode("store_A");
      System.out.println(storeA);
      System.out.println(storeA.getMarket());
      return null;
    };

    transactionTemplate.execute(doInTransaction);
    assertThat(QueryCountHolder.getGrandTotal().getSelect()).isEqualTo(2);

    QueryCountHolder.clear();

    System.out.println(">>>> 2nd transaction:");
    transactionTemplate.execute(doInTransaction);
    assertThat(QueryCountHolder.getGrandTotal().getSelect()).isZero();

    Cache cache = entityManager.getEntityManagerFactory().getCache().unwrap(Cache.class);
    assertThat(cache.containsEntity(Warehouse.class, 100L)).isFalse();
    assertThat(cache.containsEntity(Market.class, 300L)).isTrue();
    assertThat(cache.containsEntity(Store.class, 400L)).isTrue();
  }

  @Test
  void getMarket_withQueryCache_and_getOneToManyRelation_with_2ndLevelCache_WORKS() {
    System.out.println(">>>> 1st transaction:");
    TransactionCallback<Object> doInTransaction = res -> {
      Market marketA = marketRepository.findByCode("market_A");
      System.out.println(marketA);
      System.out.println(marketA.getStores());
      return null;
    };

    transactionTemplate.execute(doInTransaction);
    assertThat(QueryCountHolder.getGrandTotal().getSelect()).isEqualTo(2);

    QueryCountHolder.clear();

    System.out.println(">>>> 2nd transaction:");
    transactionTemplate.execute(doInTransaction);
    assertThat(QueryCountHolder.getGrandTotal().getSelect()).isZero();

    Cache cache = entityManager.getEntityManagerFactory().getCache().unwrap(Cache.class);
    assertThat(cache.containsEntity(Warehouse.class, 100L)).isFalse();
    assertThat(cache.containsEntity(Market.class, 300L)).isTrue();
    assertThat(cache.containsEntity(Store.class, 400L)).isTrue();
  }


  @Test
  void findAll_WithoutQueryCache_triggersSQLQueryOnEachRetrieval() {
    System.out.println("NO QUERY CACHE");

    System.out.println(">>>> 1st transaction:");
    TransactionCallback<Object> doInTransaction = res -> {
      System.out.println(storeRepository.findAll());
      return null;
    };

    transactionTemplate.execute(doInTransaction);
    assertThat(QueryCountHolder.getGrandTotal().getSelect()).isEqualTo(1);

    QueryCountHolder.clear();

    System.out.println(">>>> 2nd transaction:");
    transactionTemplate.execute(doInTransaction);
    assertThat(QueryCountHolder.getGrandTotal().getSelect()).isEqualTo(1);

    QueryCountHolder.clear();

    System.out.println(">>>> 3nd transaction:");
    transactionTemplate.execute(doInTransaction);
    assertThat(QueryCountHolder.getGrandTotal().getSelect()).isEqualTo(1);

    Cache cache = entityManager.getEntityManagerFactory().getCache().unwrap(Cache.class);
    assertThat(cache.containsEntity(Warehouse.class, 100L)).isFalse();
    assertThat(cache.containsEntity(Market.class, 300L)).isFalse();
    assertThat(cache.containsEntity(Store.class, 400L)).isTrue();
  }


  //Expected to fail
  @Test
  void getWarehouse_and_getOneToOneRelation_2ndLevelCache_NOT_Works() {
    // 2nd Level Cache does not work for "mappedBy" OneToOne relationship

    System.out.println(">>>> 1st transaction:");
    TransactionCallback<Object> doInTransaction = res -> {
      Warehouse warehouseA = warehouseRepository.findByCode("warehouse_A");
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

  //Expected to fail
  @Test
  void getMarket_and_getOneToOneRelation_2ndLevelCache_NOT_Works() {
    System.out.println(">>>> 1st transaction:");
    TransactionCallback<Object> doInTransaction = res -> {
      Market marketA = marketRepository.findByCode("market_A");
      System.out.println(marketA);
      System.out.println(marketA.getWarehouse().getCode());
      return null;
    };

    transactionTemplate.execute(doInTransaction);

    assertThat(QueryCountHolder.getGrandTotal().getSelect()).isEqualTo(2);

    QueryCountHolder.clear();

    System.out.println(">>>> 2nd transaction:");
    transactionTemplate.execute(doInTransaction);
    assertThat(QueryCountHolder.getGrandTotal().getSelect()).isZero();

    Cache cache = entityManager.getEntityManagerFactory().getCache().unwrap(Cache.class);
    assertThat(cache.containsEntity(Warehouse.class, 1L)).isTrue();
    assertThat(cache.containsEntity(Market.class, 1L)).isTrue();
    assertThat(cache.containsEntity(Store.class, 1L)).isFalse();
  }
}
