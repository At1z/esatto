package com.atis.esatto.controller;

import com.atis.esatto.db_creation.Product;
import com.atis.esatto.exceptions.ProductNotFoundException;
import com.atis.esatto.service.ProductService;
import com.atis.esatto.dto.ProductDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:5137")
public class ProductController {


    private final ProductService productService;

    // curl.exe -X POST http://localhost:8080/products -H "Content-Type: application/json" -d "{\"baseCurrency\":\"USD\",\"targetCurrency\":\"EUR\",\"cost\":25.25}"
    @PostMapping
    public Product addProduct(@RequestBody @Valid ProductDTO productDTO) {
        return productService.addProduct(productDTO);
    }

    // curl.exe -X PUT http://localhost:8080/products/1 -H "Content-Type: application/json" -d "{\"baseCurrency\":\"USD\",\"targetCurrency\":\"EUR\",\"cost\":30.00}"
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        Product updatedProduct = new Product();
        updatedProduct.setBaseCurrency(productDTO.getBaseCurrency());
        updatedProduct.setTargetCurrency(productDTO.getTargetCurrency());
        updatedProduct.setCost(productDTO.getCost());

        return productService.updateProduct(id, updatedProduct);
    }

    // curl.exe -X DELETE http://localhost:8080/products/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);

        return ResponseEntity.noContent().build();
    }

    // curl.exe -X GET http://localhost:8080/products
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // curl.exe -X GET http://localhost:8080/products/1
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        return ResponseEntity.ok(product);
    }



    // curl.exe -X GET "http://localhost:8080/products/sorted?sortBy=date"
    // curl.exe -X GET "http://localhost:8080/products/sorted?sortBy=baseCurrency"
    // curl.exe -X GET "http://localhost:8080/products/sorted?sortBy=targetCurrency"
    @GetMapping("/sorted")
    public List<Product> getSortedProducts(@RequestParam(defaultValue = "date") String sortBy) {
        return productService.getSortedProducts(sortBy);
    }

    // curl.exe -X GET "http://localhost:8080/products/search?baseCurrency=USD&targetCurrency=EUR&maxCost=1.5"
    @GetMapping("/search")
    public List<Product> searchProducts(
            @RequestParam(required = false) String baseCurrency,
            @RequestParam(required = false) String targetCurrency,
            @RequestParam(required = false) Double maxCost) {

        return productService.searchProducts(baseCurrency, targetCurrency, maxCost);
    }

    @GetMapping("/paged")
    public Page<Product> getPagedProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String baseCurrency,
            @RequestParam(required = false) String targetCurrency) {

        return productService.getPagedProducts(page, size, baseCurrency, targetCurrency);
    }



}
