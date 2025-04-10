package com.atis.esatto.logic;

import com.atis.esatto.db_creation.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Stream;

@Component
public class SearchingLogic {

    private static final Logger logger = LoggerFactory.getLogger(SearchingLogic.class);

    public Stream<Product> searchingBy(String baseCurrency, String targetCurrency, Double maxCost, List<Product> allProducts){
        logger.info("Searching products with criteria - baseCurrency: {}, targetCurrency: {}, maxCost: {}",
                baseCurrency, targetCurrency, maxCost);
        logger.debug("Starting search with {} products", allProducts.size());

        Stream<Product> productStream = allProducts.stream();

        if (baseCurrency != null && !baseCurrency.isEmpty()) {
            logger.debug("Filtering by base currency: {}", baseCurrency);
            productStream = productStream.filter(p -> p.getBaseCurrency().equalsIgnoreCase(baseCurrency));
        }

        if (targetCurrency != null && !targetCurrency.isEmpty()) {
            logger.debug("Filtering by target currency: {}", targetCurrency);
            productStream = productStream.filter(p -> p.getTargetCurrency().equalsIgnoreCase(targetCurrency));
        }

        if (maxCost != null) {
            logger.debug("Filtering by maximum cost: {}", maxCost);
            productStream = productStream.filter(p -> p.getCost() <= maxCost);
        }

        logger.debug("Search filtering complete");
        return productStream;
    }
}