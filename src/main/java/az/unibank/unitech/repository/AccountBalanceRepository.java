package az.unibank.unitech.repository;

import az.unibank.unitech.entity.AccountBalance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountBalanceRepository extends JpaRepository<AccountBalance, Integer> {
}