package com.atis.esatto.dto;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductDTO {


    private LocalDateTime date;

    @NotBlank(message = "Base currency code is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Base currency must consist of 3 uppercase letters (e.g., USD)")
    private String baseCurrency;

    @NotBlank(message = "Target currency code is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Target currency must consist of 3 uppercase letters (e.g., EUR)")
    private String targetCurrency;


    private Boolean cheaper;

    @NotNull(message = "Cost cannot be null")
    @Positive(message = "Cost must be greater than 0")
    private Double cost;
}

