package com.atis.esatto.service;

import com.atis.esatto.api.APICaller;
import com.atis.esatto.db_creation.Product;
import com.atis.esatto.dto.ProductDTO;
import com.atis.esatto.factory.ProductFactory;
import com.atis.esatto.repository.ProductRepository;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class APIService {


    private final ProductRepository productRepository;
    private final ProductFactory productFactory;
    private final APICaller apiCaller;

    public Product getExchangeRate(String base, String target) {
        try {
            APICaller.ExchangeRateResponse response = apiCaller.getExchangeRate(base, target);

            ProductDTO productDTO = new ProductDTO();
            productDTO.setBaseCurrency(response.baseCurrency());
            productDTO.setTargetCurrency(target);
            productDTO.setCost(response.rateValue());

            Product savedProduct = productFactory.createProduct(productDTO, productRepository);
            updateCheaperFlag(savedProduct, productDTO.getCost());

            return savedProduct;

        } catch (Exception e) {
            throw new RuntimeException("Error retrieving exchange rate: " + e.getMessage(), e);
        }
    }

    private void updateCheaperFlag(Product product, Double cost) {
        List<Product> productsByCurrencyPair = productRepository.findByBaseCurrencyAndTargetCurrencyOrderByDateDesc(
                product.getBaseCurrency(), product.getTargetCurrency());

        System.out.println(productsByCurrencyPair);
        if (productsByCurrencyPair.size() <= 1) {
            return;
        }

        Product previousProduct = null;
        for (int i = 0; i < productsByCurrencyPair.size() ; i++) {
            if (!productsByCurrencyPair.get(i).getId().equals(product.getId())) {
                previousProduct = productsByCurrencyPair.get(i);
                break;
            }
        }

        System.out.println(previousProduct);
        if (previousProduct != null) {
            if (cost < previousProduct.getCost()) {
                product.setCheaper(true);
                productRepository.save(product);
            }
        }
    }
}