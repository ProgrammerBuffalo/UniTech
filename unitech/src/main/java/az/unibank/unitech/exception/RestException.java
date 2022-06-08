package az.unibank.unitech.exception;

import az.unibank.unitech.exception.constant.ErrorConstants;
import org.springframework.http.HttpStatus;

public class RestException extends Throwable {

    private HttpStatus statusCode;
    public RestException(ErrorConstants errorConstant) {
        super(errorConstant.getMessage());
        this.statusCode = errorConstant.getHttpStatus();
    }

    public static RestException of(ErrorConstants error) {
        return new RestException(error);
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }
}
