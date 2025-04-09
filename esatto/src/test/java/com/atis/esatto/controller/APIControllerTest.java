package com.atis.esatto.controller;

import com.atis.esatto.db_creation.Product;
import com.atis.esatto.service.APIService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;



@ExtendWith(MockitoExtension.class)
class APIControllerTest {

    @Mock
    private APIService apiService;

    @InjectMocks
    private APIController apiController;

    @Test
    void getExchangeRate_ShouldReturnProduct() {
        // Given
        Product expected = new Product();
        expected.setBaseCurrency("USD");
        expected.setTargetCurrency("PLN");
        when(apiService.getExchangeRate(anyString(), anyString())).thenReturn(expected);

        // When
        Product result = apiController.getExchangeRate("USD", "PLN");

        // Then
        assertEquals("USD", result.getBaseCurrency());
        assertEquals("PLN", result.getTargetCurrency());
    }

    @Test
    void getExchangeRate_ShouldReturnProduct2() {
        // Given
        Product expected = new Product();
        expected.setBaseCurrency("PLN");
        expected.setTargetCurrency("USD");
        when(apiService.getExchangeRate(anyString(), anyString())).thenReturn(expected);

        // When
        Product result = apiController.getExchangeRate("PLN", "USD");

        // Then
        assertEquals("PLN", result.getBaseCurrency());
        assertEquals("USD", result.getTargetCurrency());
    }

    @Test
    void getExchangeRate_ShouldCallServiceOnce() {
        // Given
        Product expected = new Product();
        when(apiService.getExchangeRate(anyString(), anyString())).thenReturn(expected);

        // When
        apiController.getExchangeRate("USD", "PLN");

        // Then
        verify(apiService, times(1)).getExchangeRate("USD", "PLN");
    }

    @Test
    void getExchangeRate_WithNullParameters_ShouldThrowException() {
        // Given
        when(apiService.getExchangeRate(null, null)).thenThrow(new IllegalArgumentException());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            apiController.getExchangeRate(null, null);
        });
    }

    @Test
    void getExchangeRate_WithEmptyCurrency_ShouldReturnProduct() {
        // Given
        Product expected = new Product();
        when(apiService.getExchangeRate("", "")).thenReturn(expected);

        // When
        Product result = apiController.getExchangeRate("", "");

        // Then
        assertNotNull(result);
    }
}