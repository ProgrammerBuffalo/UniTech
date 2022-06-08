package az.unibank.unitech.service;

import az.unibank.unitech.dto.response.AccountResponse;
import az.unibank.unitech.dto.response.BalanceTransferResponse;
import az.unibank.unitech.entity.Account;
import az.unibank.unitech.entity.AccountBalance;
import az.unibank.unitech.entity.ExchangeRate;
import az.unibank.unitech.exception.RestException;
import az.unibank.unitech.exception.constant.ErrorConstants;
import az.unibank.unitech.repository.*;
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

    @Transactional
    public BalanceTransferResponse transfer(String userToken, Integer fromPIN, Integer toPIN, Double money) throws RestException {

        if(fromPIN.equals(toPIN))
            throw RestException.of(ErrorConstants.SAME_ACCOUNT_TRANSFER);

        UUID userId = tokenRepository.findById(UUID.fromString(userToken))
                .get().getId();

        Account fromAcc = accountRepository.findByPinAndUserId(fromPIN, userId)
                        .orElseThrow(() -> RestException.of(ErrorConstants.PIN_NOT_FOUND));

        if(!fromAcc.getActive())
            throw RestException.of(ErrorConstants.ACCOUNT_IS_NOT_ACTIVE);

        if(fromAcc.getAccountBalance().getCash() < money)
            throw RestException.of(ErrorConstants.NOT_ENOUGH_MONEY);

        Account toAcc = accountRepository.findByPin(toPIN)
                .orElseThrow(() -> RestException.of(ErrorConstants.PIN_NOT_FOUND));

        if(!toAcc.getActive())
            throw RestException.of(ErrorConstants.ACCOUNT_IS_NOT_ACTIVE);

        AccountBalance fromAccBalance = accountBalanceRepository
                .findById(fromAcc.getAccountBalance().getId())
                .get();

        AccountBalance toAccBalance = accountBalanceRepository
                .findById(toAcc.getAccountBalance().getId())
                .get();

        ExchangeRate exchangeRateFromTo = exchangeRateRepository
                .findByFromAndTo(fromAccBalance.getCurrency(), toAccBalance.getCurrency())
                .get();

        ExchangeRate exchangeRateToFrom = exchangeRateRepository
                .findByFromAndTo(toAccBalance.getCurrency(), fromAccBalance.getCurrency())
                .get();

        Double cashFromAccBalance = fromAccBalance.getCash() - money * exchangeRateFromTo.getRate();

        Double cashToAccBalance = toAccBalance.getCash() + money * exchangeRateToFrom.getRate();

        accountBalanceRepository.save(fromAccBalance.setCash(cashFromAccBalance));
        accountBalanceRepository.save(toAccBalance.setCash(cashToAccBalance));

        return new BalanceTransferResponse()
                .setSenderPin(fromAcc.getPin())
                .setLeftCashOfSender(cashFromAccBalance)
                .setReceiverPin(toAcc.getPin())
                .setNewCashOfReceiver(cashToAccBalance);
    }
}
