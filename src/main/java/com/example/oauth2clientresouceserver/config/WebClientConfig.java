package com.example.oauth2clientresouceserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

@Configuration
public class WebClientConfig {
    @Bean
    WebClient webClient(ClientRegistrationRepository clientRegistrationRepository, OAuth2AuthorizedClientRepository authorizedClientRepository) {
        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 = new ServletOAuth2AuthorizedClientExchangeFilterFunction(
                clientRegistrationRepository, authorizedClientRepository);
        return WebClient.builder()
                .apply(oauth2.oauth2Configuration())
                .build();
    }

    @Bean
    OAuth2AuthorizedClientRepository authorizedClientRepository(OAuth2AuthorizedClientService authorizedClientService) {
        return new OAuth2AuthorizedClientRepository() {
            final String DEFAULT_PRINCIPAL_NAME = "noms-auditor";
            final Authentication DEFAULT_PRINCIPAL = new PrincipalNameAuthentication(DEFAULT_PRINCIPAL_NAME);

            @Override
            public <T extends OAuth2AuthorizedClient> T loadAuthorizedClient(String clientRegistrationId, Authentication principal, HttpServletRequest request) {
                return authorizedClientService.loadAuthorizedClient(clientRegistrationId, DEFAULT_PRINCIPAL_NAME);
            }

            @Override
            public void saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal, HttpServletRequest request, HttpServletResponse response) {
                authorizedClientService.saveAuthorizedClient(authorizedClient, DEFAULT_PRINCIPAL);
            }

            @Override
            public void removeAuthorizedClient(String clientRegistrationId, Authentication principal, HttpServletRequest request, HttpServletResponse response) {
                authorizedClientService.removeAuthorizedClient(clientRegistrationId, DEFAULT_PRINCIPAL_NAME);
            }
        };
    }

    private static class PrincipalNameAuthentication implements Authentication {
        private final String username;

        private PrincipalNameAuthentication(String username) {
            this.username = username;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            throw unsupported();
        }

        @Override
        public Object getCredentials() {
            throw unsupported();
        }

        @Override
        public Object getDetails() {
            throw unsupported();
        }

        @Override
        public Object getPrincipal() {
            throw unsupported();
        }

        @Override
        public boolean isAuthenticated() {
            throw unsupported();
        }

        @Override
        public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
            throw unsupported();
        }

        @Override
        public String getName() {
            return this.username;
        }

        private UnsupportedOperationException unsupported() {
            return new UnsupportedOperationException("Not Supported");
        }
    }


}
