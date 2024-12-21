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
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/t")
public class AuthenticationController {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
    AuthenticationService authenticationService;

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> authenticated(@RequestBody AuthenticationRequest request){
        log.info("auth controller");
        return authenticationService.authenticate(request);
    }

    @PostMapping("/validate")
    public ApiResponse<ValidateTokenResponse> validateToken(@RequestBody ValidateTokenRequest request) throws ParseException, JOSEException {
        var response = authenticationService.validateToken(request);
        return ApiResponse.<ValidateTokenResponse>builder()
                .result(response)
                .build();
    }

    @PostMapping("/logout")
    public void logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
    }

    @GetMapping("/get-accId-from-token")
    public ApiResponse<String> getAccountIdFromToken(@RequestHeader("Authorization") String token) throws ParseException {
        String newToken = token.replace("Bearer ", "");
        return authenticationService.getAccountIdFromToken(newToken);
    }
}
