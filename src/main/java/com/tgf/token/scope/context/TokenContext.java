package com.tgf.token.scope.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface TokenContext {
    Map<String, Token> tokenMap = new ConcurrentHashMap<>();

    default String getToken(String tokenType) {
        return tokenMap.get(tokenType).getToken();
    }

    void putIfAbsent(Token token, String uniqueKey);

    void remove(String uniqueKey);
}
