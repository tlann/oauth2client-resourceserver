package com.example.oauth2clientresouceserver;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({OAuth2ClientConfig.class})
public @interface EnableExampleResourceServerClient {
}
