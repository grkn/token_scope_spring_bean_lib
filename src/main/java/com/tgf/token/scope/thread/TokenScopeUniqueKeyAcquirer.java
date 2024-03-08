package com.tgf.token.scope.thread;

public final class TokenScopeUniqueKeyAcquirer {

    private TokenScopeUniqueKeyAcquirer() {
    }

    private static final ThreadLocal<String> CURRENT_UNIQUE_KEY = new ThreadLocal<>();
    
    public static void put(String uniqueKey) {
        CURRENT_UNIQUE_KEY.set(uniqueKey);
    }
    
    public static String get() {
        return CURRENT_UNIQUE_KEY.get();
    }
}
