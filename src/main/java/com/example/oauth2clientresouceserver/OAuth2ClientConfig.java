package com.example.oauth2clientresouceserver;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class OAuth2ClientConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.oauth2Client();

        http
                .csrf().disable()
                .anonymous().disable()
                .httpBasic().disable()
                .logout().disable()
                .formLogin().disable()
                .authorizeRequests()
                  .antMatchers(HttpMethod.OPTIONS).permitAll()
                  .antMatchers("/actuator/**").access("#oauth2.hasScope('auditor')")
                  .anyRequest().authenticated()
                .and()
                .oauth2ResourceServer().jwt();
    }

}
