package com.va.vinassets.services;

import com.va.vinassets.dao.CryptoRepository;
import com.va.vinassets.dao.StockRepository;
import com.va.vinassets.models.Crypto;
import com.va.vinassets.models.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PortfolioService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private CryptoRepository cryptoRepository;

    // Fetch all stocks
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    // Fetch all cryptos
    public List<Crypto> getAllCryptos() {
        return cryptoRepository.findAll();
    }

    // Add a new stock
    public Stock addStock(Stock stock) {
        return stockRepository.save(stock);
    }

    // Add a new crypto
    public Crypto addCrypto(Crypto crypto) {
        return cryptoRepository.save(crypto);
    }

    // Update stock
    public Stock updateStock(Stock stock) {
        return stockRepository.save(stock);
    }

    // Update crypto
    public Crypto updateCrypto(Crypto crypto) {
        return cryptoRepository.save(crypto);
    }

    // Delete stock by ID
    public void deleteStock(Long id) {
        stockRepository.deleteById(id);
    }

    // Delete crypto by ID
    public void deleteCrypto(Long id) {
        cryptoRepository.deleteById(id);
    }
}

