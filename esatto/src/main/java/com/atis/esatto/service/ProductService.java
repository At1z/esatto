package com.atis.esatto.service;


import com.atis.esatto.db_creation.Product;
import com.atis.esatto.dto.ProductDTO;
import com.atis.esatto.exceptions.ProductNotFoundException;
import com.atis.esatto.factory.ProductFactory;
import com.atis.esatto.logic.PagedLogic;
import com.atis.esatto.logic.SearchingLogic;
import com.atis.esatto.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.atis.esatto.logic.SortingLogic;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ProductService {


    private final ProductRepository productRepository;
    private final ProductFactory productFactory;
    private final SortingLogic sortingBy;
    private final SearchingLogic searchingBy;
    private final PagedLogic paged;

    public Product addProduct(ProductDTO productDTO) {
        Product savedProduct = productFactory.createProduct(productDTO, productRepository);
        updateCheaperFlag(savedProduct, productDTO.getCost());
        return savedProduct;
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        Optional<Product> existingProductOpt = productRepository.findById(id);

        if (existingProductOpt.isEmpty()) {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }

        Product existingProduct = existingProductOpt.get();
        existingProduct.setDate(LocalDateTime.now());
        existingProduct.setBaseCurrency(updatedProduct.getBaseCurrency());
        existingProduct.setTargetCurrency(updatedProduct.getTargetCurrency());
        existingProduct.setCost(updatedProduct.getCost());
        existingProduct.setCheaper(false);

        Product savedProduct = productRepository.save(existingProduct);

        updateCheaperFlag(savedProduct, updatedProduct.getCost());

        return savedProduct;
    }

    private void updateCheaperFlag(Product product, Double cost) {
        List<Product> productsByCurrencyPair = productRepository.findByBaseCurrencyAndTargetCurrencyOrderByDateDesc(
                product.getBaseCurrency(), product.getTargetCurrency());

        System.out.println(productsByCurrencyPair);
        if (productsByCurrencyPair.size() <= 1) {
            return;
        }

        Product previousProduct = null;
        for (int i = 0; i < productsByCurrencyPair.size() ; i++) {
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

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
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
        return paged.paged(page, size, baseCurrency, targetCurrency, productRepository);
    }
}