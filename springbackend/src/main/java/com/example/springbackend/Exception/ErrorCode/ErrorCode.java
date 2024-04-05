package com.example.springbackend.Exception.ErrorCode;

import org.springframework.http.HttpStatus;

// 에러 코드 추상화
public interface ErrorCode {

    String name();
    HttpStatus getHttpStatus();
    String getMessage();
}
