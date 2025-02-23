package com.xthreads.post_service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_EXISTED(2001, "Username existed.", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(2002, "Username not existed.", HttpStatus.NOT_FOUND),
    CANNOT_GENERATE_TOKEN(2003, "Cannot generate token.", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(2004, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    FILE_NOT_EXISTED(2005, "File not existed.", HttpStatus.BAD_REQUEST),
    POST_NOT_EXISTED(2006, "Post not existed.", HttpStatus.BAD_REQUEST),
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized Exception", HttpStatus.INTERNAL_SERVER_ERROR),
    CANNOT_ADD_REACTION(2007, "Cannot add reaction.", HttpStatus.BAD_REQUEST);
    private int code;
    private String message;
    private HttpStatusCode statusCode;
}
