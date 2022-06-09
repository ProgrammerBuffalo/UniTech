package com.eldareyvazov.unitech;

import com.eldareyvazov.unitech.entity.Currency;
import com.eldareyvazov.unitech.entity.ExchangeRate;
import com.eldareyvazov.unitech.exception.RestException;
import com.eldareyvazov.unitech.repository.CurrencyRepository;
import com.eldareyvazov.unitech.repository.ExchangeRateRepository;
import com.eldareyvazov.unitech.service.ExchangeRateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ExchangeRateServiceTest {

    @InjectMocks
    private ExchangeRateService exchangeRateService;

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @Mock
    private CurrencyRepository currencyRepository;

    @Test
    void setExchangeRate_Verify_IfValid() throws RestException {
        String from = Mockito.any();
        String to = Mockito.any();

        ExchangeRate exchangeRate = new ExchangeRate();

        Mockito.when(exchangeRateRepository.findByFromNameAndToName(from, to))
                .thenReturn(Optional.empty());

        Mockito.when(currencyRepository.findByNameIgnoreCase(Mockito.any()))
                .thenReturn(Optional.of(new Currency()));

        Mockito.when(exchangeRateRepository.save(Mockito.any()))
                .thenReturn(exchangeRate);

        exchangeRateService.setExchangeRate(from, to, 1.0);

        Mockito.verify(exchangeRateRepository, Mockito.times(1)).findByFromNameAndToName(from, to);
        Mockito.verify(currencyRepository, Mockito.times(2)).findByNameIgnoreCase(Mockito.any());
        Mockito.verify(exchangeRateRepository, Mockito.times(1)).save(Mockito.any());

    }
}