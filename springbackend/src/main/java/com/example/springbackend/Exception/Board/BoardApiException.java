package com.example.springbackend.Exception.Board;

import com.example.springbackend.Exception.ErrorCode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BoardApiException extends RuntimeException {
    private final ErrorCode errorCode;
}