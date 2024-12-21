package com.xthreads.auth_service.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.xthreads.auth_service.dto.request.AuthenticationRequest;
import com.xthreads.auth_service.dto.request.LogoutRequest;
import com.xthreads.auth_service.dto.request.ValidateTokenRequest;
import com.xthreads.auth_service.dto.response.ApiResponse;
import com.xthreads.auth_service.dto.response.AuthenticationResponse;
import com.xthreads.auth_service.dto.response.ValidateTokenResponse;
import com.xthreads.auth_service.entity.Account;
import com.xthreads.auth_service.entity.InvalidatedToken;
import com.xthreads.auth_service.exception.AppException;
import com.xthreads.auth_service.exception.ErrorCode;
import com.xthreads.auth_service.repository.AccountRepository;
import com.xthreads.auth_service.repository.InvalidatedTokenRepository;
import com.xthreads.auth_service.repository.client.UserClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    AccountRepository accountRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;
    PasswordEncoder passwordEncoder;
    UserClient userClient;

    @NonFinal
    @Value("${jwt.valid-duration}")
    private int VALID_DURATION;

    @NonFinal
    @Value("${jwt.signerKey}")
    private String SIGNER_KEY;

    public ApiResponse<String> getAccountIdFromToken(String token) throws ParseException {
        SignedJWT signedJWT = verifyToken(token, true);
        return ApiResponse.<String>builder()
                .result(signedJWT.getJWTClaimsSet().getSubject())
                .build();
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        SignedJWT signedJWT = verifyToken(request.getToken(), false);
        log.info(signedJWT.serialize());
        String tokenId = signedJWT.getJWTClaimsSet().getJWTID();
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(tokenId)
                .expiryTime(expiryTime)
                .build();
        invalidatedTokenRepository.save(invalidatedToken);
    }

    public ValidateTokenResponse validateToken(ValidateTokenRequest request) throws ParseException, JOSEException {
        String token = request.getToken();
        boolean isValid = true;
        try{
            verifyToken(token, true);
        }catch (Exception e){
            isValid = false;
        }
        log.info(String.valueOf(isValid));
        return ValidateTokenResponse.builder()
                .valid(isValid)
                        .build();
    }

    public SignedJWT verifyToken(String token, boolean flag) {
        try{
            JWSVerifier jwsVerifier = new MACVerifier(SIGNER_KEY.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);
            boolean verified = signedJWT.verify(jwsVerifier);
            log.info("hi: " + verified);
            if(!verified || invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            log.info("h1");
            Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

            if(expiryTime.before(new Date()) && flag){
                invalidatedTokenRepository.save(InvalidatedToken.builder()
                        .id(signedJWT.getJWTClaimsSet().getJWTID())
                        .expiryTime(signedJWT.getJWTClaimsSet().getExpirationTime())
                        .build());
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }

            return signedJWT;
        } catch (ParseException | JOSEException e) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

    }

    public ApiResponse<AuthenticationResponse> authenticate(AuthenticationRequest request){
        log.info("authenticate.........................");
        Account account = accountRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_EXISTED));
        if(passwordEncoder.matches(request.getPassword(), account.getPassword())){
            return ApiResponse.<AuthenticationResponse>builder()
                    .result(AuthenticationResponse.builder()
                            .token(generateToken(account))
                            .build())
                    .build();
        }
        else {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

    public String generateToken(Account account){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(account.getId())
                .issuer("DevKir")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(account))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new AppException(ErrorCode.CANNOT_GENERATE_TOKEN);
        }
    }

    private String buildScope(Account account){
        StringJoiner stringJoiner = new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(account.getRoles())) {
            account.getRoles().stream().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
            });
        }
        return stringJoiner.toString();
    }
}
