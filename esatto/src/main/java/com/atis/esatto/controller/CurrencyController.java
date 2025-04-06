package com.atis.esatto.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;

@RestController
@RequestMapping("/currency")
public class CurrencyController {

    private static final String API_URL = "https://www.ratexchanges.com/api/v1/exchange-rates"; // Exchange rates API URL

    @Value("${currency.api.key}")
    private String apiKey;  // Inject the API key from application.properties

    @PostMapping("/exchange-rates")
    public String getExchangeRate(@RequestParam String base, @RequestParam String target) {
        RestTemplate restTemplate = new RestTemplate();

        // Prepare the form data to send in the request
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("base", base);  // Base currency
        map.add("target", target);  // Target currency

        // Set up the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        headers.set("Cache-Control", "no-cache");
        headers.set("api_key", apiKey);  // Use the injected API key here

        // Create the request entity with the parameters and headers
        URI uri = URI.create(API_URL);
        RequestEntity<MultiValueMap<String, String>> requestEntity = new RequestEntity<>(map, headers, HttpMethod.POST, uri);

        // Send the request and get the response as a String
        ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

        try {
            // Parse the JSON response
            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);

            // Extract the base currency
            String baseCurrency = rootNode.get("base").asText();

            // Extract the value of the exchange rate
            String value = rootNode.get("rate").get(0).get("value").asText();

            // Extract the time and get the year, month, and day
            String time = rootNode.get("time").asText();
            String[] timeParts = time.split(" ")[0].split("-");  // Split the date (yyyy-MM-dd)

            String year = timeParts[0];
            String month = timeParts[1];
            String day = timeParts[2];

            // Return the extracted information
            return "Base: " + baseCurrency + ", Value: " + value + ", Date: " + year + "-" + month + "-" + day;

        } catch (Exception e) {
            return "Error processing the response: " + e.getMessage();
        }
    }
}
