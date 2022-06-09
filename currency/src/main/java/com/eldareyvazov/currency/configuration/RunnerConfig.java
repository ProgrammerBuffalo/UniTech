package com.eldareyvazov.currency.configuration;

import com.eldareyvazov.currency.model.CurrencyRate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Configuration
public class RunnerConfig {

    public static List<CurrencyRate> rateList =
            Arrays.stream(new CurrencyRate[]{
                    new CurrencyRate("USD", "AZN", 1.7),
                    new CurrencyRate("USD", "TLY", 17.58),
                    new CurrencyRate("AZN", "USD", 0.59),
                    new CurrencyRate("AZN", "TLY", 10.11),
                    new CurrencyRate("TLY", "USD", 0.058),
                    new CurrencyRate("TLY", "AZN", 0.1)
    }).collect(Collectors.toList());

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }

    @Bean
    public CommandLineRunner backgroundRunner(TaskExecutor taskExecutor) {
        return args -> taskExecutor.execute(() -> {
            try {
                while (true) {
                    Random random = new Random();
                    rateList.stream()
                            .forEach(currencyRate -> currencyRate
                                    .setRate(currencyRate.getRate() + random.nextDouble(0.1) - 0.01));

                    Thread.sleep(1000 * 60);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
