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
        newProduct.setCurrency(productDTO.getCurrency());
        newProduct.setCost(productDTO.getCost());
        newProduct.setCheaper(false);

        Product savedProduct = productRepository.save(newProduct);

        updateCheaperFlag(savedProduct, productDTO.getCurrency(), productDTO.getCost());

        return savedProduct;
    }

    public Product updateExistingProduct(Long id, Product updatedProduct) {
        Optional<Product> existingProductOpt = productRepository.findById(id);

        if (existingProductOpt.isEmpty()) {
            return null;
        }

        Product existingProduct = existingProductOpt.get();
        existingProduct.setDate(LocalDate.now());
        existingProduct.setCurrency(updatedProduct.getCurrency());
        existingProduct.setCost(updatedProduct.getCost());
        existingProduct.setCheaper(false);

        Product savedProduct = productRepository.save(existingProduct);

        updateCheaperFlag(savedProduct, updatedProduct.getCurrency(), updatedProduct.getCost());

        return savedProduct;
    }

    private void updateCheaperFlag(Product product, String currency, Double cost) {
        List<Product> productsByCurrency = productRepository.findByCurrencyOrderByDateDesc(currency);

        if (productsByCurrency.size() <= 1) {
            return;
        }

        // Znajdź poprzedni produkt (który nie jest aktualnie aktualizowanym produktem)
        Product previousProduct = null;
        for (int i = productsByCurrency.size() - 1; i >= 0; i--) {
            if (!productsByCurrency.get(i).getId().equals(product.getId())) {
                previousProduct = productsByCurrency.get(i);
                break;
            }
        }
        System.out.println(previousProduct);
        // Jeśli istnieje poprzedni produkt i obecny koszt jest niższy
        if (previousProduct != null && cost < previousProduct.getCost()) {
            product.setCheaper(true);
            productRepository.save(product);
        }
    }
}