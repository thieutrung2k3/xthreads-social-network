package com.xthreads.notification_service.dto.response;

public class ApiResponse <T>{
    int code;
    String message;
    T result;
}
