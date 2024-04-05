package com.example.springbackend.Exception.User;

import com.example.springbackend.Exception.ErrorCode.ErrorCode;
import com.example.springbackend.Exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // 응답(에러 메시지)을 JSON 형식으로 반환
public class UserExceptionAdvice {

    @ExceptionHandler(UserApiException.class)
    public ResponseEntity<Object> handleCustomUserException(UserApiException e){
        ErrorCode errorCode = e.getErrorCode();
        return handleExceptionInternal(errorCode);
    }

    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode));
    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .name(errorCode.name())
                .message(errorCode.getMessage())
                .build();
    }
}
