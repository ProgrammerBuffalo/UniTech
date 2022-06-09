package com.eldareyvazov.unitech.repository;

import com.eldareyvazov.unitech.entity.AccountBalance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountBalanceRepository extends JpaRepository<AccountBalance, Integer> {
}