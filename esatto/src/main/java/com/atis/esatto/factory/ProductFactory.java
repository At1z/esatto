package com.atis.esatto.factory;

import com.atis.esatto.db_creation.Product;
import com.atis.esatto.dto.ProductDTO;
import com.atis.esatto.repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
public class ProductFactory {
    public Product createProduct(ProductDTO productDTO, ProductRepository productRepository) {
        Product newProduct = new Product();
        newProduct.setDate(LocalDateTime.now());
        newProduct.setBaseCurrency(productDTO.getBaseCurrency());
        newProduct.setTargetCurrency(productDTO.getTargetCurrency());
        newProduct.setCost(productDTO.getCost());
        newProduct.setCheaper(false);

        return productRepository.save(newProduct);
    }
}