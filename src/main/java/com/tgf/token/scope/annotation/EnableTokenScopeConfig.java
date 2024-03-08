package com.tgf.token.scope.annotation;

import com.tgf.token.scope.config.ScopeRegistrationConfig;
import com.tgf.token.scope.context.TokenContextHolder;
import com.tgf.token.scope.filter.TokenScopeFilter;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(value = {TokenScopeFilter.class, TokenContextHolder.class, ScopeRegistrationConfig.class})
public @interface EnableTokenScopeConfig {

}
