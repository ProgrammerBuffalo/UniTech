package az.unibank.unitech.rest;

import az.unibank.unitech.dto.response.rest.CurrencyRatesRestResponse;
import az.unibank.unitech.entity.Currency;
import az.unibank.unitech.entity.ExchangeRate;
import az.unibank.unitech.repository.CurrencyRepository;
import az.unibank.unitech.repository.ExchangeRateRepository;
import az.unibank.unitech.service.ExchangeRateService;
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
