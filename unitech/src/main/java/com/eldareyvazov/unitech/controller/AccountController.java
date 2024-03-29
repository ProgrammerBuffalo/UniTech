package com.eldareyvazov.unitech.controller;

import com.eldareyvazov.unitech.dto.base.BaseResponse;
import com.eldareyvazov.unitech.dto.response.AccountResponse;
import com.eldareyvazov.unitech.dto.response.BalanceTransferResponse;
import com.eldareyvazov.unitech.exception.RestException;
import com.eldareyvazov.unitech.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/transfer/from/{from}/to/{to}")
    public ResponseEntity<BaseResponse<?>> transfer(@RequestHeader("token") String token, @PathVariable("from") Integer from, @PathVariable("to") Integer to, @RequestParam("money") Double money) throws RestException {
        BalanceTransferResponse balanceTransferResponse = accountService.transfer(token, from, to, money);
        return ResponseEntity.ok(BaseResponse
                .success(balanceTransferResponse, "Money successfully transfered", HttpStatus.OK));
    }
}
