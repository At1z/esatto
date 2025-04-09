package com.atis.esatto.logic;

import com.atis.esatto.db_creation.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Component
public class SearchingLogic {


    public Stream<Product> searchingBy(String baseCurrency, String targetCurrency, Double maxCost, List<Product> allProducts){

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
