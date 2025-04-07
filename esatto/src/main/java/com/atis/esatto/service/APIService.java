package com.atis.esatto.service;

import com.atis.esatto.api.APICaller;
import com.atis.esatto.db_creation.Product;
import com.atis.esatto.dto.ProductDTO;
import com.atis.esatto.factory.ProductFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class APIService {

    @Autowired
    private ProductFactory productFactory;

    @Autowired
    private APICaller apiCaller;

    public Product getExchangeRate(String base, String target) {
        try {
            APICaller.ExchangeRateResponse response = apiCaller.getExchangeRate(base, target);

            ProductDTO productDTO = new ProductDTO();
            productDTO.setBaseCurrency(response.getBaseCurrency()); // Changed from setCurrency
            productDTO.setTargetCurrency(target); // Added new field
            productDTO.setCost(response.getRateValue());

            return productFactory.createProduct(productDTO);

        } catch (Exception e) {
            throw new RuntimeException("Error retrieving exchange rate: " + e.getMessage(), e);
        }
    }
}