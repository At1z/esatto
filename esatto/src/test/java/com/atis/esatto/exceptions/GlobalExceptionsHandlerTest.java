package com.atis.esatto.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
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
}