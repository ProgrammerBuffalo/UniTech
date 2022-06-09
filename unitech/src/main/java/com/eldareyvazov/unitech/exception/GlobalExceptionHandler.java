package com.eldareyvazov.unitech.exception;

import com.eldareyvazov.unitech.dto.base.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RestException.class)
    public ResponseEntity<BaseResponse<?>> handleRestEx(RestException restException) {
        return ResponseEntity
                .status(restException.getStatusCode())
                .body(BaseResponse.fault(restException.getMessage(), restException.getStatusCode().value()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<?>> handleEx(Exception exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(BaseResponse.fault("Something goes wrong", 500));
    }
}
