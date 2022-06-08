package az.unibank.unitech.exception.constant;

import org.springframework.http.HttpStatus;

public enum ErrorConstants {

    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "Token not found"),
    TOKEN_IS_EXPIRED(HttpStatus.UNAUTHORIZED, "Token is expired"),
    TOKEN_IS_REVOKED(HttpStatus.UNAUTHORIZED, "Token is revoked");

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
