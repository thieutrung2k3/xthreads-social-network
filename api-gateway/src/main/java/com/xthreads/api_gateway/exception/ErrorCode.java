package com.xthreads.api_gateway.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    UNAUTHENTICATED(1101, "Unauthenticated.")
    ;
    private int code;
    private String message;
}
