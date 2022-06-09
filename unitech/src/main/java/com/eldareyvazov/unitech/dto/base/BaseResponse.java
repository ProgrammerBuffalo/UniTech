package com.eldareyvazov.unitech.dto.base;

import org.springframework.http.HttpStatus;

public class BaseResponse<T> {

    private T data;

    private String message;

    private Integer statusCode;

    public static BaseResponse<?> success(String message, HttpStatus statusCode) {
        return new BaseResponse<>()
                .setMessage(message)
                .setStatusCode(statusCode.value());
    }

    public static <T> BaseResponse<T> success(T data, String message, HttpStatus statusCode) {
        return new BaseResponse<T>()
                .setData(data)
                .setMessage(message)
                .setStatusCode(statusCode.value());
    }

    public static BaseResponse<?> fault(String message, Integer statusCode) {
        return new BaseResponse<>()
                .setMessage(message)
                .setStatusCode(statusCode);
    }

    public T getData() {
        return data;
    }

    public BaseResponse<T> setData(T data) {
        this.data = data;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public BaseResponse<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public BaseResponse<T> setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
        return this;
    }
}
