package com.eldareyvazov.currency.model;

public class CurrencyRate {

    private String from;

    private String to;

    private Double rate;

    public CurrencyRate(String from, String to, Double rate) {
        this.from = from;
        this.to = to;
        this.rate = rate;
    }

    public String getFrom() {
        return from;
    }

    public CurrencyRate setFrom(String from) {
        this.from = from;
        return this;
    }

    public String getTo() {
        return to;
    }

    public CurrencyRate setTo(String to) {
        this.to = to;
        return this;
    }

    public Double getRate() {
        return rate;
    }

    public CurrencyRate setRate(Double rate) {
        this.rate = rate;
        return this;
    }
}
