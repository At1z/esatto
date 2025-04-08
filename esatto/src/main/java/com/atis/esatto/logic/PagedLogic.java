package com.atis.esatto.logic;

import com.atis.esatto.db_creation.Product;
import com.atis.esatto.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class PagedLogic {

    public Page<Product> paged(int page, int size, String baseCurrency, String targetCurrency, ProductRepository productRepository ){
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        if (baseCurrency != null && targetCurrency != null) {
            return productRepository.findByBaseCurrencyAndTargetCurrency(baseCurrency, targetCurrency, pageable);
        } else if (baseCurrency != null) {
            return productRepository.findByBaseCurrency(baseCurrency, pageable);
        } else if (targetCurrency != null) {
            return productRepository.findByTargetCurrency(targetCurrency, pageable);
        } else {
            return productRepository.findAll(pageable);
        }
    }
}
