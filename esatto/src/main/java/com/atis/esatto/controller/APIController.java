package com.atis.esatto.controller;

import com.atis.esatto.db_creation.Product;
import com.atis.esatto.service.APIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
;

@RestController
@RequestMapping("/currency")
public class APIController {

    @Autowired
    private APIService apiService;

    // curl.exe -X POST http://localhost:8080/currency/exchange-rates -H "Content-Type: application/x-www-form-urlencoded" -d "base=USD&target=PLN"
    @PostMapping("/exchange-rates")
    public Product getExchangeRate(@RequestParam String base, @RequestParam String target) {
        return apiService.getExchangeRate(base, target);
    }

}
