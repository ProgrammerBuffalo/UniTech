package az.unibank.unitech.repository;

import az.unibank.unitech.entity.Currency;
import az.unibank.unitech.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Integer> {

    Optional<ExchangeRate> findByFromAndTo(Currency from, Currency to);
}
