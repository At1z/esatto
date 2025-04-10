package com.atis.esatto.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.dao.DataAccessException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import java.util.Collections;
import static org.mockito.Mockito.when;


class GlobalExceptionsHandlerTest {

    private final GlobalExceptionsHandler handler = new GlobalExceptionsHandler();

    @Test
    void handleValidationException_ShouldReturnBadRequest() {
        // Given
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("object", "field", "message");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));

        // When
        ResponseEntity<ValidationErrorResponse> response = handler.handleValidationException(ex);

        // Then
        assertEquals(400, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    void handleProductNotFoundException_ShouldReturnNotFound() {
        // Given
        ProductNotFoundException ex = new ProductNotFoundException("Not found");

        // When
        ResponseEntity<String> response = handler.handleProductNotFound(ex);

        // Then
        assertEquals(404, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("Not found"));
    }


    @Test
    void handleDataAccessException_ShouldReturnInternalServerError() {
        // Given
        DataAccessException ex = mock(DataAccessException.class);
        when(ex.getMessage()).thenReturn("Database connection failed");

        // When
        ResponseEntity<String> response = handler.handleDataAccessException(ex);

        // Then
        assertEquals(500, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("Database connection failed"));
    }

    @Test
    void handleGenericException_ShouldReturnInternalServerError() {
        // Given
        Exception ex = new Exception("Unexpected error occurred");

        // When
        ResponseEntity<String> response = handler.handleGenericException(ex);

        // Then
        assertEquals(500, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("Unexpected error occurred"));
    }
}