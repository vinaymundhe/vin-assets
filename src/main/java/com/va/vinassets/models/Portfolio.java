package com.va.vinassets.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;  // Assuming you are tracking portfolios by user

    @OneToMany(cascade = CascadeType.ALL)
    private List<Stock> stocks;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Crypto> cryptos;

    public Portfolio() {
    }

    public Portfolio(String userId, List<Stock> stocks, List<Crypto> cryptos) {
        this.userId = userId;
        this.stocks = stocks;
        this.cryptos = cryptos;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }

    public List<Crypto> getCryptos() {
        return cryptos;
    }

    public void setCryptos(List<Crypto> cryptos) {
        this.cryptos = cryptos;
    }

    // Methods to add stocks and cryptos
    public void addStock(Stock stock) {
        this.stocks.add(stock);
    }

    public void addCrypto(Crypto crypto) {
        this.cryptos.add(crypto);
    }
}

