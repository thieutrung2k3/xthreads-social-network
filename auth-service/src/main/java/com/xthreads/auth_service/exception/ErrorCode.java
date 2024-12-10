package com.xthreads.auth_service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USERNAME_EXISTED(9001, "Username existed.", HttpStatus.BAD_REQUEST),
    USERNAME_NOT_EXISTED(9002, "Username not existed.", HttpStatus.NOT_FOUND),
    CANNOT_GENERATE_TOKEN(9003, "Cannot generate token.", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(9004, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    FILE_NOT_EXISTED(9005, "File not existed.", HttpStatus.BAD_REQUEST),
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized Exception", HttpStatus.INTERNAL_SERVER_ERROR),;
    private int code;
    private String message;
    private HttpStatusCode statusCode;
}
