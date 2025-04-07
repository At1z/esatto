package com.atis.esatto.db_creation;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "products")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private String baseCurrency; // Changed from currency

    private String targetCurrency; // New field

    private Boolean cheaper;

    private Double cost;
}