package com.atis.esatto.dto;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ProductDTO {

    private LocalDate date;
    private String currency;
    private Boolean cheaper;
    private Double cost;

}
