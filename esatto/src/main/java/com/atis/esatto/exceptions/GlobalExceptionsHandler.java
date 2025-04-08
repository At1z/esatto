package com.atis.esatto.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionsHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        ValidationErrorResponse errorResponse = new ValidationErrorResponse();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorResponse.addError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(RateNodeInvalidRuntimeException.class)
    public ResponseEntity<RuntimeErrorResponse> handleRateNodeInvalidRuntimeException(RateNodeInvalidRuntimeException ex){
        String errorMessage = ex.getMessage();

        RuntimeErrorResponse runtimeResponse = new RuntimeErrorResponse(500, errorMessage);
        return ResponseEntity.internalServerError().body(runtimeResponse);
    }
}