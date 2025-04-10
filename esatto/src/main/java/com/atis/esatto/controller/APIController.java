package com.atis.esatto.controller;

import com.atis.esatto.db_creation.Product;
import com.atis.esatto.service.APIService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/currency")
@CrossOrigin(origins = "http://localhost")
public class APIController {


    private static final Logger logger = LoggerFactory.getLogger(APIController.class);

    private final APIService apiService;

    // curl.exe -X POST http://localhost:8080/currency/exchange-rates -H "Content-Type: application/x-www-form-urlencoded" -d "base=USD&target=PLN"

    @PostMapping("/exchange-rates")
    public Product getExchangeRate(@RequestParam String base, @RequestParam String target) {
        logger.info("Received request for exchange rate: {} to {}", base, target);
        Product result = apiService.getExchangeRate(base, target);
        logger.info("Returning exchange rate product with id: {}", result.getId());
        return result;
    }
}