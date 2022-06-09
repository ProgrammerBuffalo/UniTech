package com.eldareyvazov.unitech;

import com.eldareyvazov.unitech.entity.Account;
import com.eldareyvazov.unitech.entity.AccountBalance;
import com.eldareyvazov.unitech.entity.User;
import com.eldareyvazov.unitech.entity.UserToken;
import com.eldareyvazov.unitech.exception.RestException;
import com.eldareyvazov.unitech.exception.constant.ErrorConstants;
import com.eldareyvazov.unitech.repository.AccountRepository;
import com.eldareyvazov.unitech.repository.TokenRepository;
import com.eldareyvazov.unitech.service.AccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private AccountRepository accountRepository;

    @Test
    void getAccounts_Verifies_IfValidRequest() throws RestException {
        UUID userToken = UUID.randomUUID();

        Mockito.when(tokenRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(new UserToken().setUser(new User())));

        Mockito.when(accountRepository.findAllByUserIdAndIsActive(Mockito.any(), Mockito.anyBoolean()))
                .thenReturn(new ArrayList<>());

        accountService.getAccounts(userToken.toString());

        Mockito.verify(tokenRepository, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(accountRepository, Mockito.times(1)).findAllByUserIdAndIsActive(Mockito.any(), Mockito.anyBoolean());
    }

    @Test
    void transfer_ThrowsException_IfSameAccount() throws RestException {

        Assertions.assertThrows(RestException.of(ErrorConstants.SAME_ACCOUNT_TRANSFER).getClass(),
                () -> accountService.transfer(UUID.randomUUID().toString(), 1000, 1000, 0.0));
    }

    @Test
    void transfer_ThrowsException_IfPinNotFound() throws RestException {

        Mockito.when(tokenRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(new UserToken()));

        Mockito.when(accountRepository.findByPinAndUser(Mockito.any(), Mockito.any()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(RestException.of(ErrorConstants.PIN_NOT_FOUND).getClass(),
                () -> accountService.transfer(UUID.randomUUID().toString(), 1000, 1001, 0.0));

    }

    @Test
    void transfer_ThrowsException_IfAccountIsDeactive() {
        Mockito.when(tokenRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(new UserToken()));

        Mockito.when(accountRepository.findByPinAndUser(Mockito.any(), Mockito.any()))
                .thenReturn(Optional.of(new Account().setActive(false)));

        Assertions.assertThrows(RestException.of(ErrorConstants.ACCOUNT_IS_NOT_ACTIVE).getClass(),
                () -> accountService.transfer(UUID.randomUUID().toString(), 1000, 1001, 0.0));

    }

    @Test
    void transfer_ThrowsException_IfAccountHasNotEnoughMoney() {
        Mockito.when(tokenRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(new UserToken()));

        Mockito.when(accountRepository.findByPinAndUser(Mockito.any(), Mockito.any()))
                .thenReturn(Optional.of(new Account()
                        .setActive(true)
                        .setAccountBalance(new AccountBalance()
                                .setCash(0.0))
                ));

        Assertions.assertThrows(RestException.of(ErrorConstants.ACCOUNT_IS_NOT_ACTIVE).getClass(),
                () -> accountService.transfer(UUID.randomUUID().toString(), 1000, 1001, 1.0));

    }
}
