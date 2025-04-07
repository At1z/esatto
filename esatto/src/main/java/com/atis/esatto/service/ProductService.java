package com.atis.esatto.service;


import com.atis.esatto.db_creation.Product;
import com.atis.esatto.dto.ProductDTO;
import com.atis.esatto.factory.ProductFactory;
import com.atis.esatto.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.atis.esatto.sorting.SortingLogic;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductFactory productFactory;

    @Autowired
    private SortingLogic sortingBy;

    public Product addProduct(ProductDTO productDTO) {
        return productFactory.createProduct(productDTO);
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        return productFactory.updateExistingProduct(id, updatedProduct);
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

    public List<Product> getSortedProducts(String sortBy) {
        List<Product> allProducts = sortingBy.sortingBy(sortBy);


        return allProducts;
    }
}