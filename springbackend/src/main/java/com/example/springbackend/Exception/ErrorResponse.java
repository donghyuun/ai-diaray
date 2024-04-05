package com.example.springbackend.Exception;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@RequiredArgsConstructor
public class ErrorResponse { // 에러 응답 클래스

    private final String name;
    private final String message;

}
