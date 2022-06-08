package az.unibank.unitech.controller;

import az.unibank.unitech.dto.base.BaseResponse;
import az.unibank.unitech.dto.response.AccountResponse;
import az.unibank.unitech.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/active-accounts")
    public ResponseEntity<BaseResponse<?>> getActiveAccounts(@RequestHeader("token") String token) {
        List<AccountResponse> accountResponseList = accountService.getAccounts(token);
        return ResponseEntity.ok(BaseResponse
                .success(accountResponseList, "User's pins successfully returned", HttpStatus.OK));
    }
}
