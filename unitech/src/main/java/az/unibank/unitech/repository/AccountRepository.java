package az.unibank.unitech.repository;

import az.unibank.unitech.entity.Account;
import az.unibank.unitech.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    Optional<Account> findByPin(Integer pin);

    Optional<Account> findByPinAndUser(Integer pin, User user);

    List<Account> findAllByUserIdAndIsActive(UUID userId, Boolean isActive);
}
