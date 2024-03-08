package com.tgf.token.scope.filter;

import com.tgf.token.scope.constant.PredefinedTokenType;
import com.tgf.token.scope.context.TokenContext;
import com.tgf.token.scope.repository.RefreshTokenRepository;
import com.tgf.token.scope.thread.TokenScopeUniqueKeyAcquirer;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TokenScopeFilter implements Filter {

    @Value("${token.header:Authorization}")
    private String header;
    @Value("${token.prefix:Bearer}")
    private String prefix;

    private final TokenContext tokenContext;

    private final RefreshTokenRepository refreshTokenRepository;

    public TokenScopeFilter(TokenContext tokenContext, RefreshTokenRepository refreshTokenRepository) {
        this.tokenContext = tokenContext;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authHeader = httpServletRequest.getHeader(header);
        if (authHeader != null && authHeader.contains(prefix)) {
            setAccessTokenToContext(authHeader);
            setRefreshTokenToContext();
        }
        chain.doFilter(request, response);
    }

    private void setRefreshTokenToContext() {
        String refreshToken = refreshTokenRepository.getRefreshToken();
        String uniqueRefreshTokenType = PredefinedTokenType.REFRESH_TOKEN.name() + "-" + refreshToken;
        tokenContext.putIfAbsent(() -> refreshToken, uniqueRefreshTokenType);
    }

    private void setAccessTokenToContext(String authHeader) {
        String accessToken = authHeader.replaceAll(prefix, "").trim();
        String uniqueAccessTokenType = PredefinedTokenType.ACCESS_TOKEN.name() + "-" + accessToken;
        TokenScopeUniqueKeyAcquirer.put(uniqueAccessTokenType);
        tokenContext.putIfAbsent(() -> accessToken, uniqueAccessTokenType);
    }

    @Override
    public void destroy() {
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix == null ? "" : prefix;
    }

}

