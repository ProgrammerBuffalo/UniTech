package com.eldareyvazov.unitech.dto.request;

public class AuthorizationRequest {

    private Integer pin;

    private String password;

    public Integer getPin() {
        return pin;
    }

    public void setPin(Integer pin) {
        this.pin = pin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
