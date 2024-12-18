package com.xthreads.friendship_service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    REQUEST_NOT_FOUND(2001, "Friend request not found", HttpStatus.NOT_FOUND),
    INVALID_REQUEST(2002, "Invalid request data", HttpStatus.BAD_REQUEST),
    ALREADY_ACCEPTED(2003, "Friend request already accepted", HttpStatus.BAD_REQUEST),
    REQUEST_ALREADY_HANDLED(2004, "Friend request already handled", HttpStatus.BAD_REQUEST);

    private int code;
    private String message;
    private HttpStatusCode statusCode;
}
