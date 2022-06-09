package az.unibank.unitech;

import az.unibank.unitech.dto.request.AuthorizationRequest;
import az.unibank.unitech.dto.request.RegistrationRequest;
import az.unibank.unitech.entity.*;
import az.unibank.unitech.exception.RestException;
import az.unibank.unitech.exception.constant.ErrorConstants;
import az.unibank.unitech.repository.*;
import az.unibank.unitech.service.AuthenticationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private AccountBalanceRepository accountBalanceRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    void registerAccount_ThrowsException_IfCurrencyNotFound() throws RestException {
        RegistrationRequest request = new RegistrationRequest();
        request.setCurrency(Mockito.anyString());

        Mockito.when(currencyRepository.findByNameIgnoreCase(request.getCurrency()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(RestException.of(ErrorConstants.CURRENCY_NOT_FOUND).getClass(),
                () -> authenticationService.registerAccount(request));
    }

    @Test
    void registerAccount_ThrowsException_IfPinAlreadyExists() throws RestException {
        RegistrationRequest request = new RegistrationRequest();
        request.setCurrency("AZN");
        request.setPin(Mockito.any());

        Mockito.when(currencyRepository.findByNameIgnoreCase(request.getCurrency()))
                .thenReturn(Optional.of(new Currency()));

        Mockito.when(accountRepository.findByPin(request.getPin()))
                .thenReturn(Optional.of(new Account()));

        Assertions.assertThrows(RestException.of(ErrorConstants.PIN_NOT_FOUND).getClass(),
                () -> authenticationService.registerAccount(request));
    }

    @Test
    void authorizeAccount_ThrowsException_IfInvalidPin() {
        AuthorizationRequest request = new AuthorizationRequest();
        request.setPin(Mockito.anyInt());

        Mockito.when(accountRepository.findByPin(request.getPin()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(RestException.of(ErrorConstants.INVALID_CREDENTIALS).getClass(),
                () -> authenticationService.authorizeAccount(request));
    }

    @Test
    void authorizeAccount_ThrowsException_IfInvalidPassword() {
        AuthorizationRequest request = new AuthorizationRequest();
        request.setPin(1000);
        request.setPassword(Mockito.any());

        Mockito.when(accountRepository.findByPin(request.getPin()))
                .thenReturn(Optional.of(new Account()));

        Mockito.when(userRepository.findByPassword(request.getPassword()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(RestException.of(ErrorConstants.INVALID_CREDENTIALS).getClass(),
                () -> authenticationService.authorizeAccount(request));
    }

    @Test
    void registerAccount_Verify_IfValidRequest() throws RestException {
        RegistrationRequest request = new RegistrationRequest();
        request.setCurrency("AZN");
        request.setPin(1000);

        User user = new User();
        Account account = new Account();
        AccountBalance accountBalance = new AccountBalance();

        Mockito.when(currencyRepository.findByNameIgnoreCase(request.getCurrency()))
                .thenReturn(Optional.of(new Currency()));

        Mockito.when(accountRepository.findByPin(request.getPin()))
                .thenReturn(Optional.empty());

        Mockito.when(userRepository.save(Mockito.any()))
                .thenReturn(user);

        Mockito.when(accountRepository.save(Mockito.any()))
                .thenReturn(account);

        Mockito.when(accountBalanceRepository.save(Mockito.any()))
                .thenReturn(accountBalance);

        authenticationService.registerAccount(request);

        Mockito.verify(currencyRepository, Mockito.times(1)).findByNameIgnoreCase(request.getCurrency());
        Mockito.verify(accountRepository, Mockito.times(1)).findByPin(request.getPin());
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(accountRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(accountBalanceRepository, Mockito.times(1)).save(Mockito.any());

    }

    @Test
    void authorizeAccount_Verify_IfValidRequest() throws RestException {
        AuthorizationRequest request = new AuthorizationRequest();
        request.setPin(1000);
        request.setPassword(Mockito.any());

        Mockito.when(accountRepository.findByPin(request.getPin()))
                .thenReturn(Optional.of(new Account()));

        Mockito.when(userRepository.findByPassword(request.getPassword()))
                .thenReturn(Optional.of(new User().setId(UUID.randomUUID())));

        Mockito.when(tokenRepository.save(Mockito.any()))
                .thenReturn(new UserToken().setId(UUID.randomUUID()));

        authenticationService.authorizeAccount(request);

        Mockito.verify(accountRepository, Mockito.times(1)).findByPin(request.getPin());
        Mockito.verify(userRepository, Mockito.times(1)).findByPassword(request.getPassword());
        Mockito.verify(tokenRepository, Mockito.times(1)).save(Mockito.any());

    }

}
