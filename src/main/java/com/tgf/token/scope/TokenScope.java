package com.tgf.token.scope;

import com.tgf.token.scope.constant.PredefinedTokenType;
import com.tgf.token.scope.context.Token;
import com.tgf.token.scope.context.TokenContextHolder;
import com.tgf.token.scope.thread.TokenScopeUniqueKeyAcquirer;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TokenScope<T extends Token> implements Scope {

    private final Map<String, Object> beans = new ConcurrentHashMap<>();

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        String key = TokenScopeUniqueKeyAcquirer.get();
        if (key == null) {
            return null;
        }
        Token token = TokenContextHolder.tokenMap.get(key);
        Object scopedObject = beans.get(token.getToken());
        if (scopedObject == null) {
            scopedObject = objectFactory.getObject();
            beans.put(token.getToken(), scopedObject);
            Object retrievedObject = beans.get(token.getToken());
            if (retrievedObject != null) {
                scopedObject = retrievedObject;
            } else {
                scopedObject = beans.getOrDefault(token.getToken(), null);
            }
        }
        return scopedObject;
    }

    @Override
    public Object remove(String name) {
        Token token = TokenContextHolder.tokenMap.get(TokenScopeUniqueKeyAcquirer.get());
        Object removedObject = beans.getOrDefault(token.getToken(), null);
        beans.remove(token.getToken());
        return removedObject;
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
    }

    @Override
    public Object resolveContextualObject(String key) {
        return beans.get(PredefinedTokenType.ACCESS_TOKEN.name());
    }

    @Override
    public String getConversationId() {
        return "tokenScope";
    }
}
