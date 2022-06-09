package com.eldareyvazov.unitech.service;

import com.eldareyvazov.unitech.entity.Currency;
import com.eldareyvazov.unitech.entity.ExchangeRate;
import com.eldareyvazov.unitech.repository.CurrencyRepository;
import com.eldareyvazov.unitech.repository.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExchangeRateService {

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    public void setExchangeRate(String from, String to, Double rate) {
        ExchangeRate exchangeRate = exchangeRateRepository
                .findByFromNameAndToName(from, to)
                .orElseGet(() -> {
                    Currency fromCurrency = currencyRepository.findByNameIgnoreCase(from).get();
                    Currency toCurrency = currencyRepository.findByNameIgnoreCase(to).get();

                    return new ExchangeRate()
                            .setFrom(fromCurrency)
                            .setTo(toCurrency);
                });

        exchangeRate.setRate(rate);

        exchangeRateRepository.save(exchangeRate);
    }
}
