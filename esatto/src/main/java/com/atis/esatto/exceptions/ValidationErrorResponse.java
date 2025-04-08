package com.atis.esatto.exceptions;

import java.util.HashMap;
import java.util.Map;

public class ValidationErrorResponse {
    private Map<String,String> fieldErrors = new HashMap<>();

    public void addError(String field, String errorMessage){
        fieldErrors.put(field,errorMessage);
    }
}
