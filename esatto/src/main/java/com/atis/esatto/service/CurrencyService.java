package com.atis.esatto.service;

import com.atis.esatto.db_creation.Product;
import com.atis.esatto.dto.ProductDTO;
import com.atis.esatto.repository.ProductRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@Service
public class CurrencyService {

    @Autowired
    private ProductRepository productRepository;

    @Value("${currency.api.key}")
    private String apiKey;

    private static final String API_URL = "https://www.ratexchanges.com/api/v1/exchange-rates";

    public Product addProduct(ProductDTO productDTO) {
        List<Product> allProducts = productRepository.findByCurrencyOrderByDateDesc(productDTO.getCurrency());
        Product newProduct = new Product();
        newProduct.setDate(LocalDate.now());
        newProduct.setCurrency(productDTO.getCurrency());
        newProduct.setCost(productDTO.getCost());
        newProduct.setCheaper(false);

        if (!allProducts.isEmpty()) {
            Product lastProduct = allProducts.get(allProducts.size() - 1);
            if (productDTO.getCost() < lastProduct.getCost()) {
                newProduct.setCheaper(true);
            }
        }

        return productRepository.save(newProduct);
    }

    public String getExchangeRate(String base, String target) {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("base", base);
        map.add("target", target);

        // Prepare request headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        headers.set("Cache-Control", "no-cache");
        headers.set("api_key", apiKey);

        URI uri = URI.create(API_URL);
        RequestEntity<MultiValueMap<String, String>> requestEntity = new RequestEntity<>(map, headers, HttpMethod.POST, uri);

        ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

        try {
            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);

            String baseCurrency = rootNode.get("base").asText();
            String value = rootNode.get("rate").get(0).get("value").asText();
            String time = rootNode.get("time").asText();

            String[] timeParts = time.split(" ")[0].split("-");
            String year = timeParts[0];
            String month = timeParts[1];
            String day = timeParts[2];

            return "Base: " + baseCurrency + ", Value: " + value + ", Date: " + year + "-" + month + "-" + day;

        } catch (Exception e) {
            return "Error processing the response: " + e.getMessage();
        }
    }
}
