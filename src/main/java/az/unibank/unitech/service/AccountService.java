package az.unibank.unitech.service;

import az.unibank.unitech.dto.response.AccountResponse;
import az.unibank.unitech.entity.Account;
import az.unibank.unitech.repository.AccountRepository;
import az.unibank.unitech.repository.TokenRepository;
import az.unibank.unitech.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TokenRepository tokenRepository;

    public List<AccountResponse> getAccounts(String userToken) {

        UUID userId = tokenRepository.findById(UUID.fromString(userToken))
                .get().getId();

        List<Account> accounts = accountRepository.findAllByUserIdAndIsActive(userId, true);

        List<AccountResponse> accountResponseList = accounts
                .stream()
                .map(account -> new AccountResponse()
                        .setPin(account.getPin())
                        .setCash(account.getAccountBalance().getCash())
                        .setCurrency(account.getAccountBalance().getCurrency().toString())
                ).collect(Collectors.toList());

        return accountResponseList;

    }
}
