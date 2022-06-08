package com.unibank.currency.controller;

import com.unibank.currency.configuration.RunnerConfig;
import com.unibank.currency.model.CurrencyRate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/currency-rates")
public class CurrencyRateController {

    @GetMapping
    public ResponseEntity<?> getRates() {
        return ResponseEntity.ok(RunnerConfig.rateList);
    }
}
