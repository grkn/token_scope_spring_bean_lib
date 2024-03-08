package com.tgf.token.scope.context;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

@Component
@ApplicationScope
public class TokenContextHolder implements TokenContext {

    @Override
    public void putIfAbsent(Token token, String uniqueTokenKey) {
        tokenMap.putIfAbsent(uniqueTokenKey, token);
    }

    @Override
    public void remove(String uniqueKey) {
        tokenMap.remove(uniqueKey);
    }

}
