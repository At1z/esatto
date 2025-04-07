package com.atis.esatto.repository;

import com.atis.esatto.db_creation.Product;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

// ProductRepository.java
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Changed method to use baseCurrency instead of currency
    List<Product> findByBaseCurrencyOrderByDateDesc(String baseCurrency);
}