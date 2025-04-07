package com.atis.esatto.repository;

import com.atis.esatto.db_creation.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByBaseCurrencyOrderByDateDesc(String baseCurrency);
    List<Product> findByBaseCurrencyAndTargetCurrencyOrderByDateDesc(String baseCurrency, String targetCurrency);
    Page<Product> findByBaseCurrency(String baseCurrency, Pageable pageable);
    Page<Product> findByTargetCurrency(String targetCurrency, Pageable pageable);
    Page<Product> findByBaseCurrencyAndTargetCurrency(String baseCurrency, String targetCurrency, Pageable pageable);

}