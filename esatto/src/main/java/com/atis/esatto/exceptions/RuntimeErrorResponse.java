package com.atis.esatto.exceptions;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RuntimeErrorResponse {
    private int statusCode;
    private String message;

}
