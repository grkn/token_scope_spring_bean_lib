package com.tgf.token.scope.resource;

import com.tgf.token.scope.context.Token;

public class TokenResource implements Token {
    private String tokenType;
    private String tokenValue;

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    @Override
    public String getToken() {
        return tokenValue;
    }
}
