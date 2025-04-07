package com.atis.esatto.controller;

import com.atis.esatto.db_creation.Product;
import com.atis.esatto.service.ProductService;
import com.atis.esatto.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // curl.exe -X POST http://localhost:8080/products -H "Content-Type: application/json" -d "{\"baseCurrency\":\"USD\",\"targetCurrency\":\"EUR\",\"cost\":25.25}"
    @PostMapping
    public Product addProduct(@RequestBody ProductDTO productDTO) {
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
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    // curl.exe -X GET http://localhost:8080/products
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // curl.exe -X GET http://localhost:8080/products/1
    @GetMapping("/{id}")
    public Optional<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    // New endpoint for sorted products
    // curl.exe -X GET "http://localhost:8080/products/sorted?sortBy=date"
    // curl.exe -X GET "http://localhost:8080/products/sorted?sortBy=baseCurrency"
    // curl.exe -X GET "http://localhost:8080/products/sorted?sortBy=targetCurrency"
    @GetMapping("/sorted")
    public List<Product> getSortedProducts(@RequestParam(defaultValue = "date") String sortBy) {
        return productService.getSortedProducts(sortBy);
    }
}
