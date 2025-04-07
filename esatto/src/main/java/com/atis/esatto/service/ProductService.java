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

        Product newProduct = new Product();
        newProduct.setDate(LocalDate.now());
        newProduct.setCurrency(productDTO.getCurrency());
        newProduct.setCost(productDTO.getCost());
        newProduct.setCheaper(false);

        Product savedProduct = productRepository.save(newProduct);

        List<Product> updatedAllProducts = productRepository.findByCurrencyOrderByDateDesc(productDTO.getCurrency());
        System.out.println("Updated list of products after adding: " + updatedAllProducts);

        if (updatedAllProducts.size() > 1) {
            Product lastProduct = updatedAllProducts.get(updatedAllProducts.size() - 2);
            System.out.println(lastProduct);
            if (productDTO.getCost() < lastProduct.getCost()) {
                savedProduct.setCheaper(true);
                savedProduct = productRepository.save(savedProduct);
            }
        }

        return savedProduct;
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        Optional<Product> existingProduct = productRepository.findById(id);

        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            product.setDate(LocalDate.now());
            product.setCurrency(updatedProduct.getCurrency());
            product.setCost(updatedProduct.getCost());
            product.setCheaper(false);

            Product savedProduct = productRepository.save(product);

            List<Product> updatedAllProducts = productRepository.findByCurrencyOrderByDateDesc(updatedProduct.getCurrency());
            System.out.println("Updated list of products after update: " + updatedAllProducts);

            if (!updatedAllProducts.isEmpty()) {
                Product lastProduct = updatedAllProducts.get(updatedAllProducts.size() - 1);

                for (int i = updatedAllProducts.size() - 1; i >= 1; i--) {
                    if (!updatedAllProducts.get(i).getId().equals(product.getId())) {
                        lastProduct = updatedAllProducts.get(i);
                        break;
                    }
                }

                System.out.println("Comparing last product ID: " + lastProduct.getId() + " with current product ID: " + product.getId());

                if (updatedProduct.getCost() < lastProduct.getCost()) {
                    product.setCheaper(true);
                    savedProduct = productRepository.save(savedProduct);
                }
            }

            return savedProduct;
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
