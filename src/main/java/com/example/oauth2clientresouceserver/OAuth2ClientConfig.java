package com.example.oauth2clientresouceserver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.web.reactive.function.client.WebClient;

@EnableWebSecurity
@Configuration
public class OAuth2ClientConfig extends WebSecurityConfigurerAdapter {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    @Bean
    public JwtDecoder jwtDecoder() {
        return JwtDecoders.fromOidcIssuerLocation(issuerUri);
    }

    @Bean
    WebClient webClient(ClientRegistrationRepository clientRegistrationRepository, OAuth2AuthorizedClientRepository authorizedClientRepository) {
        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 = new ServletOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrationRepository, authorizedClientRepository);
        oauth2.setDefaultOAuth2AuthorizedClient(true);
        return WebClient.builder()
                .apply(oauth2.oauth2Configuration())
                .build();
    }

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
