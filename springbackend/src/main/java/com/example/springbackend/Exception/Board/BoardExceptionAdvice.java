package com.example.springbackend.Exception.Board;

import com.example.springbackend.Exception.ErrorCode.ErrorCode;
import com.example.springbackend.Exception.ErrorResponse;
import com.example.springbackend.Exception.User.UserApiException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class BoardExceptionAdvice {
    @ExceptionHandler(BoardApiException.class)
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
