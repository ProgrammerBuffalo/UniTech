package com.eldareyvazov.unitech.loader;

import com.eldareyvazov.unitech.entity.Currency;
import com.eldareyvazov.unitech.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataLoader implements ApplicationRunner {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        currencyRepository.saveAll(Arrays
                .asList(
                        new Currency().setName("AZN"),
                        new Currency().setName("USD"),
                        new Currency().setName("EUR"),
                        new Currency().setName("TLY")));
    }
}