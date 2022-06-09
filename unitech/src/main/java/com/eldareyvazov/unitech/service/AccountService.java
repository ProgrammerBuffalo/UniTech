package com.eldareyvazov.unitech.service;

import com.eldareyvazov.unitech.dto.response.AccountResponse;
import com.eldareyvazov.unitech.dto.response.BalanceTransferResponse;
import com.eldareyvazov.unitech.entity.Account;
import com.eldareyvazov.unitech.entity.AccountBalance;
import com.eldareyvazov.unitech.entity.ExchangeRate;
import com.eldareyvazov.unitech.entity.User;
import com.eldareyvazov.unitech.exception.RestException;
import com.eldareyvazov.unitech.exception.constant.ErrorConstants;
import com.eldareyvazov.unitech.repository.AccountBalanceRepository;
import com.eldareyvazov.unitech.repository.AccountRepository;
import com.eldareyvazov.unitech.repository.ExchangeRateRepository;
import com.eldareyvazov.unitech.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountBalanceRepository accountBalanceRepository;

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    public List<AccountResponse> getAccounts(String userToken) {

        UUID userId = tokenRepository.findById(UUID.fromString(userToken))
                .get().getUser().getId();

        List<Account> accounts = accountRepository.findAllByUserIdAndIsActive(userId, true);

        List<AccountResponse> accountResponseList = accounts
                .stream()
                .map(account -> new AccountResponse()
                        .setPin(account.getPin())
                        .setCash(account.getAccountBalance().getCash())
                        .setCurrency(account.getAccountBalance().getCurrency().getName())
                ).collect(Collectors.toList());

        return accountResponseList;

    }

    @Transactional
    public BalanceTransferResponse transfer(String userToken, Integer fromPIN, Integer toPIN, Double money) throws RestException {

        if(fromPIN.equals(toPIN))
            throw RestException.of(ErrorConstants.SAME_ACCOUNT_TRANSFER);

        User user = tokenRepository.findById(UUID.fromString(userToken))
                .get().getUser();

        Optional<Account> fromAcc = accountRepository.findByPinAndUser(fromPIN, user);

        if(!fromAcc.isPresent())
            throw RestException.of(ErrorConstants.PIN_NOT_FOUND);

        if(!fromAcc.get().getActive())
            throw RestException.of(ErrorConstants.ACCOUNT_IS_NOT_ACTIVE);

        Account toAcc = accountRepository.findByPin(toPIN)
                .orElseThrow(() -> RestException.of(ErrorConstants.PIN_NOT_FOUND));

        if(!toAcc.getActive())
            throw RestException.of(ErrorConstants.ACCOUNT_IS_NOT_ACTIVE);

        AccountBalance fromAccBalance = accountBalanceRepository
                .findById(fromAcc.get().getAccountBalance().getId())
                .get();

        AccountBalance toAccBalance = accountBalanceRepository
                .findById(toAcc.getAccountBalance().getId())
                .get();

        if(fromAcc.get().getAccountBalance().getCash() < money)
            throw RestException.of(ErrorConstants.NOT_ENOUGH_MONEY);

        Double cashFromAccBalance = 0.0;
        Double cashToAccBalance = 0.0;

        if(!fromAccBalance.getCurrency().equals(toAccBalance.getCurrency())) {

            ExchangeRate exchangeRateFromTo = exchangeRateRepository
                    .findByFromAndTo(fromAccBalance.getCurrency(), toAccBalance.getCurrency())
                    .get();

            ExchangeRate exchangeRateToFrom = exchangeRateRepository
                    .findByFromAndTo(toAccBalance.getCurrency(), fromAccBalance.getCurrency())
                    .get();

            cashFromAccBalance = fromAccBalance.getCash() - money * exchangeRateFromTo.getRate();
            cashToAccBalance = toAccBalance.getCash() + money * exchangeRateToFrom.getRate();
        }
        else {
            cashFromAccBalance = fromAccBalance.getCash() - money;
            cashToAccBalance = toAccBalance.getCash() + money;
        }

        if(cashFromAccBalance < 0)
            throw RestException.of(ErrorConstants.NOT_ENOUGH_MONEY);

        accountBalanceRepository.save(fromAccBalance.setCash(cashFromAccBalance));
        accountBalanceRepository.save(toAccBalance.setCash(cashToAccBalance));

        return new BalanceTransferResponse()
                .setSenderPin(fromAcc.get().getPin())
                .setLeftCashOfSender(cashFromAccBalance)
                .setReceiverPin(toAcc.getPin())
                .setNewCashOfReceiver(cashToAccBalance);
    }
}
