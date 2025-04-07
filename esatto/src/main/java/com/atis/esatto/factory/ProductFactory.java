package com.atis.esatto.factory;

import com.atis.esatto.db_creation.Product;
import com.atis.esatto.dto.ProductDTO;
import com.atis.esatto.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class ProductFactory {

    @Autowired
    private ProductRepository productRepository;

    public Product createProduct(ProductDTO productDTO) {
        Product newProduct = new Product();
        newProduct.setDate(LocalDate.now());
        newProduct.setBaseCurrency(productDTO.getBaseCurrency());
        newProduct.setTargetCurrency(productDTO.getTargetCurrency());
        newProduct.setCost(productDTO.getCost());
        newProduct.setCheaper(false);

        Product savedProduct = productRepository.save(newProduct);

        updateCheaperFlag(savedProduct, productDTO.getBaseCurrency(), productDTO.getCost());

        return savedProduct;
    }

    public Product updateExistingProduct(Long id, Product updatedProduct) {
        Optional<Product> existingProductOpt = productRepository.findById(id);

        if (existingProductOpt.isEmpty()) {
            return null;
        }

        Product existingProduct = existingProductOpt.get();
        existingProduct.setDate(LocalDate.now());
        existingProduct.setBaseCurrency(updatedProduct.getBaseCurrency());
        existingProduct.setTargetCurrency(updatedProduct.getTargetCurrency());
        existingProduct.setCost(updatedProduct.getCost());
        existingProduct.setCheaper(false);

        Product savedProduct = productRepository.save(existingProduct);

        updateCheaperFlag(savedProduct, updatedProduct.getBaseCurrency(), updatedProduct.getCost());

        return savedProduct;
    }

    private void updateCheaperFlag(Product product, String baseCurrency, Double cost) {
        List<Product> productsByCurrencyPair = productRepository.findByBaseCurrencyAndTargetCurrencyOrderByDateDesc(
                product.getBaseCurrency(), product.getTargetCurrency());

        System.out.println(productsByCurrencyPair);
        if (productsByCurrencyPair.size() <= 1) {
            return;
        }

        Product previousProduct = null;
        for (int i = productsByCurrencyPair.size() - 1; i >= 0 ; i--) {
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