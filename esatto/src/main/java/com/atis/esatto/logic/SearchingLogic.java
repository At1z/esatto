package com.atis.esatto.logic;

import com.atis.esatto.db_creation.Product;
import com.atis.esatto.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Component
public class SearchingLogic {
    @Autowired
    private ProductRepository productRepository;

    public Stream<Product> searchingBy(String baseCurrency, String targetCurrency, Double maxCost){
        List<Product> allProducts = productRepository.findAll();

        Stream<Product> productStream = allProducts.stream();

        if (baseCurrency != null && !baseCurrency.isEmpty()) {
            productStream = productStream.filter(p -> p.getBaseCurrency().equalsIgnoreCase(baseCurrency));
        }

        if (targetCurrency != null && !targetCurrency.isEmpty()) {
            productStream = productStream.filter(p -> p.getTargetCurrency().equalsIgnoreCase(targetCurrency));
        }

        if (maxCost != null) {
            productStream = productStream.filter(p -> p.getCost() <= maxCost);
        }
        return productStream;
    }
}
