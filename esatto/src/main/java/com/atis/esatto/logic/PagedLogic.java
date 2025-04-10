package com.atis.esatto.logic;

import com.atis.esatto.db_creation.Product;
import com.atis.esatto.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class PagedLogic {

    private static final Logger logger = LoggerFactory.getLogger(PagedLogic.class);

    public Page<Product> paged(int page, int size, String baseCurrency, String targetCurrency, ProductRepository productRepository) {
        logger.info("Retrieving paged products - page: {}, size: {}, baseCurrency: {}, targetCurrency: {}",
                page, size, baseCurrency, targetCurrency);

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        logger.debug("Created pageable request with sort by id ascending");

        Page<Product> result;
        if (baseCurrency != null && targetCurrency != null) {
            logger.debug("Searching by both base and target currency");
            result = productRepository.findByBaseCurrencyAndTargetCurrency(baseCurrency, targetCurrency, pageable);
        } else if (baseCurrency != null) {
            logger.debug("Searching by base currency only");
            result = productRepository.findByBaseCurrency(baseCurrency, pageable);
        } else if (targetCurrency != null) {
            logger.debug("Searching by target currency only");
            result = productRepository.findByTargetCurrency(targetCurrency, pageable);
        } else {
            logger.debug("No currency filters applied, returning all products");
            result = productRepository.findAll(pageable);
        }

        logger.debug("Retrieved page {} of {} with {} elements",
                result.getNumber(), result.getTotalPages(), result.getNumberOfElements());
        return result;
    }
}