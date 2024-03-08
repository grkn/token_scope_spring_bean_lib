package com.tgf.token.scope.repository;

@FunctionalInterface
public interface RefreshTokenRepository {
    String getRefreshToken();
}
