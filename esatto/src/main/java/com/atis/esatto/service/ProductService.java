package com.atis.esatto.service;

import com.atis.esatto.db_creation.Product;
import com.atis.esatto.dto.ProductDTO;
import com.atis.esatto.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product addProduct(ProductDTO productDTO) {
        List<Product> allProducts = productRepository.findByCurrencyOrderByDateDesc(productDTO.getCurrency());
        //System.out.println(allProducts);
        Product newProduct = new Product();
        newProduct.setDate(LocalDate.now());
        newProduct.setCurrency(productDTO.getCurrency());
        newProduct.setCost(productDTO.getCost());
        newProduct.setCheaper(false);

        if (!allProducts.isEmpty()) {
            Product lastProduct = allProducts.get(allProducts.size() - 1);
            if (productDTO.getCost() < lastProduct.getCost()) {
                newProduct.setCheaper(true);
            }
        }

        return productRepository.save(newProduct);
    }
    public Product updateProduct(Long id, Product updatedProduct) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            product.setDate(updatedProduct.getDate());
            product.setCurrency(updatedProduct.getCurrency());
            product.setCheaper(updatedProduct.getCheaper());
            product.setCost(updatedProduct.getCost());
            return productRepository.save(product);
        }
        return null;
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }
}
