package com.atis.esatto.logic;

import com.atis.esatto.db_creation.Product;
import com.atis.esatto.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UpdateCheaperFlag {

    private static final Logger logger = LoggerFactory.getLogger(UpdateCheaperFlag.class);

    public void updateCheaperFlag(Product product, Double cost, ProductRepository productRepository) {
        logger.debug("Updating cheaper flag for product: {} with cost: {}", product.getId(), cost);

        List<Product> productsByCurrencyPair = productRepository.findByBaseCurrencyAndTargetCurrencyOrderByDateDesc(
                product.getBaseCurrency(), product.getTargetCurrency());

        logger.debug("Found {} products with same currency pair", productsByCurrencyPair.size());

        if (productsByCurrencyPair.size() <= 1) {
            logger.debug("No previous products found for comparison, skipping flag update");
            return;
        }

        Product previousProduct = null;
        for (int i = 0; i < productsByCurrencyPair.size(); i++) {
            if (!productsByCurrencyPair.get(i).getId().equals(product.getId())) {
                previousProduct = productsByCurrencyPair.get(i);
                break;
            }
        }

        logger.debug("Previous product for comparison: {}", previousProduct);

        if (previousProduct != null) {
            if (cost < previousProduct.getCost()) {
                logger.info("Product {} is cheaper than previous product {}. Setting cheaper flag to true",
                        product.getId(), previousProduct.getId());
                product.setCheaper(true);
                productRepository.save(product);
            } else {
                logger.debug("Product is not cheaper than the previous one. Current: {}, Previous: {}",
                        cost, previousProduct.getCost());
            }
        }
    }
}