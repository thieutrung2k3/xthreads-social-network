package com.xthreads.post_service.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xthreads.post_service.dto.response.ApiResponse;
import com.xthreads.post_service.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Slf4j
public class SecurityConfig {

    @Autowired
    private CustomJwtDecoder customJwtDecoder;

    private static String[] PUBLIC_ENDPOINTS = {
            "/post/create",
            "/post/get-all",
            "/post/get/*"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        log.info("hêrrrrrrrrrrrrrrrrrreeeeeee");
        httpSecurity
                .authorizeHttpRequests(request -> request.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.GET, PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers("image/**").permitAll()
                .anyRequest()
                .authenticated());
        httpSecurity.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer
                .decoder(customJwtDecoder)
                .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                .authenticationEntryPoint((request, response, authException) -> {
                    ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;

                    response.setStatus(errorCode.getStatusCode().value());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    log.info("hêrrrrrrrrrrrrrrrrrreeeeeee");
                    ApiResponse<?> apiResponse = ApiResponse.builder()
                            .code(errorCode.getCode())
                            .message(errorCode.getMessage())
                            .build();
                    ObjectMapper objectMapper = new ObjectMapper();

                    response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
                    response.flushBuffer();
                }));
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }
}