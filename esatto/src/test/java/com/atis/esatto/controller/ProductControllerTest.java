package com.atis.esatto.controller;

import com.atis.esatto.db_creation.Product;
import com.atis.esatto.dto.ProductDTO;
import com.atis.esatto.exceptions.ProductNotFoundException;
import com.atis.esatto.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @Test
    void addProduct_ValidDTO_ShouldReturnProduct() {
        // Given
        ProductDTO dto = new ProductDTO();
        dto.setBaseCurrency("USD");
        dto.setTargetCurrency("PLN");
        dto.setCost(100.0);

        Product expected = new Product();
        expected.setBaseCurrency("USD");
        expected.setTargetCurrency("PLN");
        expected.setCost(100.0);

        when(productService.addProduct(any(ProductDTO.class))).thenReturn(expected);

        // When
        Product result = productController.addProduct(dto);

        // Then
        assertNotNull(result);
        assertEquals("USD", result.getBaseCurrency());
        assertEquals("PLN", result.getTargetCurrency());
        assertEquals(100.0, result.getCost());
        verify(productService, times(1)).addProduct(any(ProductDTO.class));
    }

    @Test
    void getProductById_ExistingId_ShouldReturnProduct() {
        // Given
        Product product = new Product();
        product.setId(1L);
        when(productService.getProductById(1L)).thenReturn(Optional.of(product));

        // When
        ResponseEntity<Product> response = productController.getProductById(1L);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void getProductById_NonExistingId_ShouldThrowException() {
        // Given
        when(productService.getProductById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ProductNotFoundException.class, () -> {
            productController.getProductById(999L);
        });
        verify(productService, times(1)).getProductById(999L);
    }

    @Test
    void deleteProduct_ShouldReturnNoContent() {
        // Given
        doNothing().when(productService).deleteProduct(1L);

        // When
        ResponseEntity<Void> response = productController.deleteProduct(1L);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(productService, times(1)).deleteProduct(1L);
    }

    @Test
    void updateProduct_ShouldReturnUpdatedProduct() {
        // Given
        ProductDTO dto = new ProductDTO();
        dto.setBaseCurrency("USD");
        dto.setTargetCurrency("EUR");
        dto.setCost(50.0);

        Product updated = new Product();
        updated.setBaseCurrency("USD");
        updated.setTargetCurrency("EUR");
        updated.setCost(50.0);

        when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(updated);

        // When
        Product result = productController.updateProduct(1L, dto);

        // Then
        assertNotNull(result);
        assertEquals("USD", result.getBaseCurrency());
        assertEquals("EUR", result.getTargetCurrency());
        assertEquals(50.0, result.getCost());
        verify(productService, times(1)).updateProduct(eq(1L), any(Product.class));
    }

    @Test
    void getAllProducts_ShouldReturnList() {
        // Given
        Product product1 = new Product();
        Product product2 = new Product();
        when(productService.getAllProducts()).thenReturn(Arrays.asList(product1, product2));

        // When
        List<Product> result = productController.getAllProducts();

        // Then
        assertEquals(2, result.size());
        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void getAllProducts_WhenEmpty_ShouldReturnEmptyList() {
        // Given
        when(productService.getAllProducts()).thenReturn(Collections.emptyList());

        // When
        List<Product> result = productController.getAllProducts();

        // Then
        assertTrue(result.isEmpty());
        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void getSortedProducts_ShouldReturnSortedList() {
        // Given
        Product product1 = new Product();
        Product product2 = new Product();
        when(productService.getSortedProducts("date")).thenReturn(Arrays.asList(product1, product2));

        // When
        List<Product> result = productController.getSortedProducts("date");

        // Then
        assertEquals(2, result.size());
        verify(productService, times(1)).getSortedProducts("date");
    }

    @Test
    void searchProducts_WithParameters_ShouldReturnFilteredList() {
        // Given
        Product product = new Product();
        when(productService.searchProducts("USD", "EUR", 100.0)).thenReturn(Collections.singletonList(product));

        // When
        List<Product> result = productController.searchProducts("USD", "EUR", 100.0);

        // Then
        assertEquals(1, result.size());
        verify(productService, times(1)).searchProducts("USD", "EUR", 100.0);
    }

    @Test
    void searchProducts_WithNullParameters_ShouldReturnAll() {
        // Given
        Product product1 = new Product();
        Product product2 = new Product();
        when(productService.searchProducts(null, null, null)).thenReturn(Arrays.asList(product1, product2));

        // When
        List<Product> result = productController.searchProducts(null, null, null);

        // Then
        assertEquals(2, result.size());
        verify(productService, times(1)).searchProducts(null, null, null);
    }
}
