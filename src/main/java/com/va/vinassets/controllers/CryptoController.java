package com.va.vinassets.controllers;

import com.va.vinassets.services.CryptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/crypto")
public class CryptoController {

    @Autowired
    private CryptoService cryptoService;

    @GetMapping("/{coinId}")
    public String getCryptoPrice(@PathVariable String coinId) {
        return cryptoService.getCryptoPrice(coinId);
    }
}

