package com.atis.esatto.service;


import com.atis.esatto.db_creation.Product;
import com.atis.esatto.dto.ProductDTO;
import com.atis.esatto.factory.ProductFactory;
import com.atis.esatto.logic.PagedLogic;
import com.atis.esatto.logic.SearchingLogic;
import com.atis.esatto.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.atis.esatto.logic.SortingLogic;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductFactory productFactory;

    @Autowired
    private SortingLogic sortingBy;

    @Autowired
    private SearchingLogic searchingBy;

    @Autowired
    private PagedLogic paged;
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
        List<Product> allProducts = productRepository.findAll();
        return sortingBy.sortingBy(sortBy, allProducts);
    }

    public List<Product> searchProducts(String baseCurrency, String targetCurrency, Double maxCost) {
        List<Product> allProducts = productRepository.findAll();
        return searchingBy.searchingBy(baseCurrency, targetCurrency, maxCost, allProducts).collect(Collectors.toList());
    }

    public Page<Product> getPagedProducts(int page, int size, String baseCurrency, String targetCurrency) {
            return paged.paged(page, size, baseCurrency, targetCurrency);
        }
}