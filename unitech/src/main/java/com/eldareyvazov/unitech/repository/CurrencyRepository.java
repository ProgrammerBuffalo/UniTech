package com.eldareyvazov.unitech.repository;

import com.eldareyvazov.unitech.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<Currency, Integer> {

    Optional<Currency> findByNameIgnoreCase(String name);
}
