package com.xthreads.friendship_service.dto.response;

public class ApiResponse <T>{
    int code;
    String message;
    T result;
}
