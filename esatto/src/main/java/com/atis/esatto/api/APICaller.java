package com.atis.esatto.api;

import com.atis.esatto.exceptions.RateNodeInvalidRuntimeException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Component
public class APICaller {

    @Value("${currency.api.key}")
    private String apiKey;

    private static final Logger logger = LoggerFactory.getLogger(APICaller.class);
    private static final String API_URL = "https://www.ratexchanges.com/api/v1/exchange-rates";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public APICaller() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public ExchangeRateResponse getExchangeRate(String base, String target) {


        RequestEntity<MultiValueMap<String, String>> requestEntity = createPostRequest(base, target, apiKey, API_URL);

        try {
            ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
            String responseBody = response.getBody();

            JsonNode rootNode = objectMapper.readTree(responseBody);

            String baseCurrency = rootNode.get("base").asText();
            JsonNode rateNode = rootNode.get("rate").get(0);

            if (rateNode == null) {
                throw new RateNodeInvalidRuntimeException("Rate data is not available in the API response");
            }

            String value = rateNode.get("value").asText();
            Double rateValue = Double.parseDouble(value);

            return new ExchangeRateResponse(baseCurrency, rateValue);

        } catch (Exception e) {
            throw new RuntimeException("Error processing the exchange rate API response: " + e.getMessage(), e);
        }
    }

    public record ExchangeRateResponse(String baseCurrency, Double rateValue) {
    }
    public static RequestEntity<MultiValueMap<String, String>> createPostRequest(
            String base, String target, String apiKey, String apiUrl) {


        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("base", base);
        requestParams.add("target", target);
        

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        headers.set("Cache-Control", "no-cache");
        headers.set("api_key", apiKey);

        URI uri = URI.create(apiUrl);

        logger.debug("Headers: {}", headers);
        logger.debug("Request Params: {}", requestParams);
        return new RequestEntity<>(requestParams, headers, HttpMethod.POST, uri);
    }

}