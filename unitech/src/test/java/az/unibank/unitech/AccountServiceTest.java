package az.unibank.unitech;

import az.unibank.unitech.dto.request.RegistrationRequest;
import az.unibank.unitech.entity.Account;
import az.unibank.unitech.entity.AccountBalance;
import az.unibank.unitech.entity.UserToken;
import az.unibank.unitech.exception.RestException;
import az.unibank.unitech.exception.constant.ErrorConstants;
import az.unibank.unitech.repository.AccountRepository;
import az.unibank.unitech.repository.TokenRepository;
import az.unibank.unitech.service.AccountService;
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
                .thenReturn(Optional.of(new UserToken()));

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

        Mockito.when(accountRepository.findByPinAndUserId(Mockito.any(), Mockito.any()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(RestException.of(ErrorConstants.PIN_NOT_FOUND).getClass(),
                () -> accountService.transfer(UUID.randomUUID().toString(), 1000, 1001, 0.0));

    }

    @Test
    void transfer_ThrowsException_IfAccountIsDeactive() {
        Mockito.when(tokenRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(new UserToken()));

        Mockito.when(accountRepository.findByPinAndUserId(Mockito.any(), Mockito.any()))
                .thenReturn(Optional.of(new Account().setActive(false)));

        Assertions.assertThrows(RestException.of(ErrorConstants.ACCOUNT_IS_NOT_ACTIVE).getClass(),
                () -> accountService.transfer(UUID.randomUUID().toString(), 1000, 1001, 0.0));

    }

    @Test
    void transfer_ThrowsException_IfAccountHasNotEnoughMoney() {
        Mockito.when(tokenRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(new UserToken()));

        Mockito.when(accountRepository.findByPinAndUserId(Mockito.any(), Mockito.any()))
                .thenReturn(Optional.of(new Account()
                        .setActive(true)
                        .setAccountBalance(new AccountBalance()
                                .setCash(0.0))
                ));

        Assertions.assertThrows(RestException.of(ErrorConstants.ACCOUNT_IS_NOT_ACTIVE).getClass(),
                () -> accountService.transfer(UUID.randomUUID().toString(), 1000, 1001, 1.0));

    }
}
