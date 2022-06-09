package com.eldareyvazov.unitech.dto.response;

public class TokenResponse {

    public String tokenValue;

    public TokenResponse(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public String getTokenValue() {
        return tokenValue;
    }

}
