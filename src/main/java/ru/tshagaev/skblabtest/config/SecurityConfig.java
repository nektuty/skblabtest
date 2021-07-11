package ru.tshagaev.skblabtest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Конфигурация аутентификации и авторизации в приложении
 *
 * @author tshagaev
 * @since 11.07.2021
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/index", "/auth/**").permitAll()
                .antMatchers("/webjars/**", "/css/**", "/img/**", "/favicon.ico").permitAll()
                .anyRequest().authenticated();
    }
}
