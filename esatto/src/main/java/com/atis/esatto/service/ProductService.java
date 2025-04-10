package com.atis.esatto.service;


import com.atis.esatto.db_creation.Product;
import com.atis.esatto.dto.ProductDTO;
import com.atis.esatto.exceptions.ProductNotFoundException;
import com.atis.esatto.factory.ProductFactory;
import com.atis.esatto.logic.PagedLogic;
import com.atis.esatto.logic.SearchingLogic;
import com.atis.esatto.logic.UpdateCheaperFlag;
import com.atis.esatto.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;
    private final ProductFactory productFactory;
    private final SortingLogic sortingBy;
    private final SearchingLogic searchingBy;
    private final PagedLogic paged;
    private final UpdateCheaperFlag updateCheaperFlag;

    public Product addProduct(ProductDTO productDTO) {
        logger.info("Adding new product with base currency: {}, target currency: {}",
                productDTO.getBaseCurrency(), productDTO.getTargetCurrency());

        Product savedProduct = productFactory.createProduct(productDTO, productRepository);
        logger.debug("Product created with ID: {}", savedProduct.getId());

        updateCheaperFlag.updateCheaperFlag(savedProduct, productDTO.getCost(), productRepository);

        logger.info("Product successfully added with ID: {}", savedProduct.getId());
        return savedProduct;
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        logger.info("Updating product with ID: {}", id);

        Optional<Product> existingProductOpt = productRepository.findById(id);

        if (existingProductOpt.isEmpty()) {
            logger.warn("Product not found with ID: {}", id);
            throw new ProductNotFoundException("Product not found with id: " + id);
        }

        Product existingProduct = existingProductOpt.get();
        logger.debug("Found existing product - baseCurrency: {}, targetCurrency: {}, cost: {}",
                existingProduct.getBaseCurrency(), existingProduct.getTargetCurrency(), existingProduct.getCost());

        // Update product fields
        existingProduct.setDate(LocalDateTime.now());
        existingProduct.setBaseCurrency(updatedProduct.getBaseCurrency());
        existingProduct.setTargetCurrency(updatedProduct.getTargetCurrency());
        existingProduct.setCost(updatedProduct.getCost());
        existingProduct.setCheaper(false);

        logger.debug("Updating to - baseCurrency: {}, targetCurrency: {}, cost: {}",
                updatedProduct.getBaseCurrency(), updatedProduct.getTargetCurrency(), updatedProduct.getCost());

        Product savedProduct = productRepository.save(existingProduct);
        logger.debug("Product updated and saved in database");

        updateCheaperFlag.updateCheaperFlag(savedProduct, updatedProduct.getCost(), productRepository);

        logger.info("Product successfully updated with ID: {}", id);
        return savedProduct;
    }


    public void deleteProduct(Long id) {
        logger.info("Deleting product with ID: {}", id);

        if (!productRepository.existsById(id)) {
            logger.warn("Product not found with ID: {}", id);
            throw new ProductNotFoundException("Product not found with id: " + id);
        }

        productRepository.deleteById(id);
        logger.info("Product successfully deleted with ID: {}", id);
    }

    public List<Product> getAllProducts() {
        logger.info("Retrieving all products");
        List<Product> products = productRepository.findAll();
        logger.debug("Retrieved {} products", products.size());
        return products;
    }

    public Optional<Product> getProductById(Long id) {
        logger.info("Retrieving product with ID: {}", id);
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            logger.debug("Product found with ID: {}", id);
        } else {
            logger.debug("No product found with ID: {}", id);
        }
        return product;
    }

    public List<Product> getSortedProducts(String sortBy) {
        logger.info("Retrieving products sorted by: {}", sortBy);
        List<Product> allProducts = productRepository.findAll();
        logger.debug("Retrieved {} products for sorting", allProducts.size());
        List<Product> sortedProducts = sortingBy.sortingBy(sortBy, allProducts);
        logger.debug("Sorted products returned");
        return sortedProducts;
    }

    public List<Product> searchProducts(String baseCurrency, String targetCurrency, Double maxCost) {
        logger.info("Searching products with criteria - baseCurrency: {}, targetCurrency: {}, maxCost: {}",
                baseCurrency, targetCurrency, maxCost);

        List<Product> allProducts = productRepository.findAll();
        logger.debug("Retrieved {} products for search", allProducts.size());

        List<Product> searchResults = searchingBy.searchingBy(baseCurrency, targetCurrency, maxCost, allProducts)
                .collect(Collectors.toList());

        logger.debug("Search returned {} products", searchResults.size());
        return searchResults;
    }

    public Page<Product> getPagedProducts(int page, int size, String baseCurrency, String targetCurrency) {
        logger.info("Retrieving paged products - page: {}, size: {}, baseCurrency: {}, targetCurrency: {}",
                page, size, baseCurrency, targetCurrency);

        Page<Product> pagedResults = paged.paged(page, size, baseCurrency, targetCurrency, productRepository);

        logger.debug("Paged results returned with {} products", pagedResults.getNumberOfElements());
        return pagedResults;
    }
}