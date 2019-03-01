package com.example.oauth2clientresouceserver;

import com.example.oauth2clientresouceserver.config.WebClientConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({OAuth2ClientConfig.class, WebClientConfig.class})
public @interface EnableExampleResourceServerClient {
}
