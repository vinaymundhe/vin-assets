package com.va.vinassets.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CryptoService {
    private final String COIN_API = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/map";

    public String getCryptoPrice(String coinId) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(COIN_API, String.class, coinId);
    }
}
