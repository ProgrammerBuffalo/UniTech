package com.eldareyvazov.unitech.rest;

import com.eldareyvazov.unitech.dto.response.rest.CurrencyRatesRestResponse;
import com.eldareyvazov.unitech.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class RestCurrencyRatesRunner {

    private final String currencyRateUrl = "http://localhost:8081/currency-rates";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ExchangeRateService exchangeRateService;

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }

    @Bean
    public CommandLineRunner backgroundRunner(TaskExecutor taskExecutor) {
        return args -> taskExecutor.execute(() -> {
            while (true) {
                List<CurrencyRatesRestResponse> rates = restTemplate
                        .exchange(currencyRateUrl,
                                HttpMethod.GET,
                                null,
                                new ParameterizedTypeReference<List<CurrencyRatesRestResponse>>() { })
                        .getBody();

                rates.stream().forEach(rate -> {
                    exchangeRateService.setExchangeRate(rate.getFrom(), rate.getTo(), rate.getRate());
                });

                try {
                    Thread.sleep(1000 * 60);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }


}
