package com.atis.esatto.db_creation;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @PastOrPresent(message = "Date cannot be in the future")
    @NotNull(message = "Date cannot be null")
    private LocalDateTime date;

    @NotBlank(message = "Base currency code is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Base currency must consist of 3 uppercase letters (e.g., USD)")
    private String baseCurrency;

    @NotBlank(message = "Target currency code is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Target currency must consist of 3 uppercase letters (e.g., EUR)")
    private String targetCurrency;

    @NotNull(message = "The 'cheaper' field cannot be null")
    private Boolean cheaper;

    @NotNull(message = "Cost cannot be null")
    @Positive(message = "Cost must be greater than 0")
    private Double cost;
}