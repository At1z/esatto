package com.atis.esatto.logic;

import com.atis.esatto.db_creation.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class SortingLogic {

    private static final Logger logger = LoggerFactory.getLogger(SortingLogic.class);

    public List<Product> sortingBy(String sortBy, List<Product> allProducts) {
        logger.info("Sorting {} products by: {}", allProducts.size(), sortBy);

        switch (sortBy.toLowerCase()) {
            case "date":
                logger.debug("Sorting by date");
                allProducts.sort(Comparator.comparing(Product::getDate));
                break;
            case "basecurrency":
                logger.debug("Sorting by base currency");
                allProducts.sort(Comparator.comparing(Product::getBaseCurrency));
                break;
            case "targetcurrency":
                logger.debug("Sorting by target currency");
                allProducts.sort(Comparator.comparing(Product::getTargetCurrency));
                break;
            default:
                logger.debug("Sorting by default (date)");
                allProducts.sort(Comparator.comparing(Product::getDate));
        }

        logger.debug("Sorting complete");
        return allProducts;
    }
}