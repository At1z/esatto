package com.atis.esatto.repository;

import com.atis.esatto.db_creation.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findTopByCurrencyOrderByDateDesc(String currency);
}
