package com.atis.esatto.service;

import com.atis.esatto.api.APICaller;
import com.atis.esatto.db_creation.Product;
import com.atis.esatto.dto.ProductDTO;
import com.atis.esatto.factory.ProductFactory;
import com.atis.esatto.logic.UpdateCheaperFlag;
import com.atis.esatto.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class APIService {

    private static final Logger logger = LoggerFactory.getLogger(APIService.class);

    private final ProductRepository productRepository;
    private final ProductFactory productFactory;
    private final APICaller apiCaller;
    private final UpdateCheaperFlag updateCheaperFlag;

    public Product getExchangeRate(String base, String target) {
        logger.info("Retrieving exchange rate for {} to {}", base, target);
        try {
            logger.debug("Calling external API for exchange rate data");
            APICaller.ExchangeRateResponse response = apiCaller.getExchangeRate(base, target);
            logger.debug("Received response with rate value: {}", response.rateValue());

            ProductDTO productDTO = new ProductDTO();
            productDTO.setBaseCurrency(response.baseCurrency());
            productDTO.setTargetCurrency(target);
            productDTO.setCost(response.rateValue());

            logger.debug("Creating and saving new product");
            Product savedProduct = productFactory.createProduct(productDTO, productRepository);
            updateCheaperFlag.updateCheaperFlag(savedProduct, productDTO.getCost(), productRepository);

            logger.info("Successfully retrieved and saved exchange rate for {} to {}", base, target);
            return savedProduct;

        } catch (Exception e) {
            logger.error("Error retrieving exchange rate: {}", e.getMessage(), e);
            throw new RuntimeException("Error retrieving exchange rate: " + e.getMessage(), e);
        }
    }
}