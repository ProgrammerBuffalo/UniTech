package com.eldareyvazov.unitech.repository;

import com.eldareyvazov.unitech.entity.Currency;
import com.eldareyvazov.unitech.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Integer> {

    Optional<ExchangeRate> findByFromAndTo(Currency from, Currency to);

    Optional<ExchangeRate> findByFromNameAndToName(String from, String to);
}
