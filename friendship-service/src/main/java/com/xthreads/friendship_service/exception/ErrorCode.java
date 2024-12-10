package com.xthreads.friendship_service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_EXISTED(1001, "Username existed.", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1002, "Username not existed.", HttpStatus.NOT_FOUND),
    CANNOT_GENERATE_TOKEN(1003, "Cannot generate token.", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1004, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    FILE_NOT_EXISTED(1005, "File not existed.", HttpStatus.BAD_REQUEST),
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized Exception", HttpStatus.INTERNAL_SERVER_ERROR),;
    private int code;
    private String message;
    private HttpStatusCode statusCode;
}
