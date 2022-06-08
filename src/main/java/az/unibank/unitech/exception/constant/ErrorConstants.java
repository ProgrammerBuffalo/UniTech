package az.unibank.unitech.exception.constant;

import org.springframework.http.HttpStatus;

public enum ErrorConstants {

    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "Token not found"),

    TOKEN_IS_EXPIRED(HttpStatus.UNAUTHORIZED, "Token is expired"),

    TOKEN_IS_REVOKED(HttpStatus.UNAUTHORIZED, "Token is revoked"),

    PIN_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "PIN already registered"),

    PIN_NOT_FOUND(HttpStatus.NOT_FOUND, "PIN not found"),

    CURRENCY_NOT_FOUND(HttpStatus.NOT_FOUND, "Currency not found"),

    INVALID_CREDENTIALS(HttpStatus.NOT_FOUND, "Invalid credentials");

    private HttpStatus httpStatus;

    private String message;

    ErrorConstants(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }

}
