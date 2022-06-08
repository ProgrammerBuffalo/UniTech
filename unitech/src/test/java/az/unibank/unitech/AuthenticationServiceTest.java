package az.unibank.unitech;

import az.unibank.unitech.dto.request.AuthorizationRequest;
import az.unibank.unitech.dto.request.RegistrationRequest;
import az.unibank.unitech.entity.Account;
import az.unibank.unitech.entity.Currency;
import az.unibank.unitech.exception.RestException;
import az.unibank.unitech.exception.constant.ErrorConstants;
import az.unibank.unitech.repository.AccountRepository;
import az.unibank.unitech.repository.CurrencyRepository;
import az.unibank.unitech.repository.UserRepository;
import az.unibank.unitech.service.AuthenticationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private AccountRepository accountRepository;

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
    void registerAccount_AssertTrue_IfValidRequest() {
        RegistrationRequest request = new RegistrationRequest();
        request.setCurrency("AZN");
        request.setPin(1000);

        Mockito.when(currencyRepository.findByNameIgnoreCase(request.getCurrency()))
                .thenReturn(Optional.of(new Currency()));

        Mockito.when(accountRepository.findByPin(request.getPin()))
                .thenReturn(Optional.empty());

        Assertions.assertDoesNotThrow(() -> authenticationService.registerAccount(request));
    }

}
