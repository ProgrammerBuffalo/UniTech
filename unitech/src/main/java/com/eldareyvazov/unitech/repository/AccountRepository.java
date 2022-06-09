package com.eldareyvazov.unitech.repository;

import com.eldareyvazov.unitech.entity.Account;
import com.eldareyvazov.unitech.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    Optional<Account> findByPin(Integer pin);

    Optional<Account> findByPinAndUser(Integer pin, User user);

    List<Account> findAllByUserIdAndIsActive(UUID userId, Boolean isActive);
}
