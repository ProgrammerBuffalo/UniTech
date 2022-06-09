package com.eldareyvazov.unitech.controller;

import com.eldareyvazov.unitech.dto.base.BaseResponse;
import com.eldareyvazov.unitech.dto.request.AuthorizationRequest;
import com.eldareyvazov.unitech.dto.request.RegistrationRequest;
import com.eldareyvazov.unitech.dto.response.TokenResponse;
import com.eldareyvazov.unitech.exception.RestException;
import com.eldareyvazov.unitech.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("authentication")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<?>> register(@RequestBody RegistrationRequest request) throws RestException {
        authenticationService.registerAccount(request);
        return ResponseEntity.ok(BaseResponse
                .success("Account successfully registered", HttpStatus.CREATED));
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<?>> login(@RequestBody AuthorizationRequest request) throws RestException {
        TokenResponse tokenResponse = authenticationService.authorizeAccount(request);
        return ResponseEntity.ok(BaseResponse
                .success(tokenResponse, "Account successfully authorized", HttpStatus.ACCEPTED));
    }
}
