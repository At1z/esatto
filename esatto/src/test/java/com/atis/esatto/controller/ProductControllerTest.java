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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
        when(productService.addProduct(any())).thenReturn(expected);

        // When
        Product result = productController.addProduct(dto);

        // Then
        assertNotNull(result);
        verify(productService, times(1)).addProduct(any());
    }

    @Test
    void getProductById_ExistingId_ShouldReturnProduct() {
        // Given
        Product product = new Product();
        when(productService.getProductById(anyLong())).thenReturn(Optional.of(product));

        // When
        ResponseEntity<Product> response = productController.getProductById(1L);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getProductById_NonExistingId_ShouldThrowException() {
        // Given
        when(productService.getProductById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ProductNotFoundException.class, () -> {
            productController.getProductById(999L);
        });
    }

    @Test
    void deleteProduct_ShouldReturnNoContent() {
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
        when(productService.updateProduct(anyLong(), any())).thenReturn(updated);

        // When
        Product result = productController.updateProduct(1L, dto);

        // Then
        assertNotNull(result);
        verify(productService, times(1)).updateProduct(eq(1L), any());
    }
}