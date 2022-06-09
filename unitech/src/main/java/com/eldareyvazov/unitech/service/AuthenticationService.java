package com.eldareyvazov.unitech.service;

import com.eldareyvazov.unitech.dto.request.AuthorizationRequest;
import com.eldareyvazov.unitech.dto.request.RegistrationRequest;
import com.eldareyvazov.unitech.dto.response.TokenResponse;
import com.eldareyvazov.unitech.exception.RestException;
import com.eldareyvazov.unitech.exception.constant.ErrorConstants;
import com.eldareyvazov.unitech.entity.*;
import com.eldareyvazov.unitech.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class AuthenticationService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountBalanceRepository accountBalanceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Transactional
    public void registerAccount(RegistrationRequest request) throws RestException {

        Optional<Currency> foundCurrency = currencyRepository.findByNameIgnoreCase(request.getCurrency());

        if(!foundCurrency.isPresent())
            throw RestException.of(ErrorConstants.CURRENCY_NOT_FOUND);

        Optional<Account> foundAccount = accountRepository.findByPin(request.getPin());

        if(foundAccount.isPresent())
            throw RestException.of(ErrorConstants.PIN_ALREADY_EXISTS);

        User user = userRepository.findByPassword(request.getPassword())
                .orElseGet(() -> userRepository.save(new User()
                        .setUsername(request.getUsername())
                        .setPassword(request.getPassword())
                ));

        Account account = accountRepository.save(new Account()
                .setPin(request.getPin())
                .setUser(user));

        accountBalanceRepository.save(new AccountBalance()
                .setAccount(account)
                .setCurrency(foundCurrency.get()));
    }

    public TokenResponse authorizeAccount(AuthorizationRequest request) throws RestException {

        Optional<Account> account = accountRepository.findByPin(request.getPin());

        if(!account.isPresent())
            throw RestException.of(ErrorConstants.INVALID_CREDENTIALS);

        Optional<User> user = userRepository.findByPassword(request.getPassword());

        if(!user.isPresent())
            throw RestException.of(ErrorConstants.INVALID_CREDENTIALS);

        UserToken userToken = tokenRepository.save(new UserToken()
                .setUser(user.get()));

        return new TokenResponse(userToken.getId().toString());
    }
}
