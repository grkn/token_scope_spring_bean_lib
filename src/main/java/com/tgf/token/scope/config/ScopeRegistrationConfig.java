package com.tgf.token.scope.config;

import com.tgf.token.scope.TokenScope;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ScopeRegistrationConfig {
    @Bean
    public static BeanFactoryPostProcessor beanFactoryPostProcessor() {
        return beanFactory -> beanFactory.registerScope("tokenScope", new TokenScope<>());
    }
}
