package com.xthreads.auth_service.controller;

import com.nimbusds.jose.JOSEException;
import com.xthreads.auth_service.dto.request.AuthenticationRequest;
import com.xthreads.auth_service.dto.request.LogoutRequest;
import com.xthreads.auth_service.dto.request.ValidateTokenRequest;
import com.xthreads.auth_service.dto.response.ApiResponse;
import com.xthreads.auth_service.dto.response.AuthenticationResponse;
import com.xthreads.auth_service.dto.response.ValidateTokenResponse;
import com.xthreads.auth_service.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/auth")
public class AuthenticationController {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
    AuthenticationService authenticationService;

    @PostMapping("/token")
    public ApiResponse<AuthenticationResponse> authenticated(@RequestBody AuthenticationRequest request){
        log.info("auth controller");
        return authenticationService.authenticate(request);
    }

    @PostMapping("/validate")
    public ApiResponse<ValidateTokenResponse> validateToken(@RequestBody ValidateTokenRequest request) throws ParseException, JOSEException {
        return authenticationService.validateToken(request);
    }

    @PostMapping("/logout")
    public void logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
    }
}
