package com.atis.esatto.controller;

import com.atis.esatto.db_creation.Product;
import com.atis.esatto.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
;

@RestController
@RequestMapping("/currency")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;


    @PostMapping("/exchange-rates")
    public Product getExchangeRate(@RequestParam String base, @RequestParam String target) {
        return currencyService.getExchangeRate(base, target);
    }

}
