package com.xthreads.post_service.dto.reponse;

public class ApiResponse <T>{
    int code;
    String message;
    T result;
}
