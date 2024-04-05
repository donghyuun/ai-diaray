package com.example.springbackend.Exception.User;

import com.example.springbackend.Exception.ErrorCode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."),
    INACTIVE_USER(HttpStatus.FORBIDDEN, "유저가 현재 비활성화 상태입니다."),
    CONFLICT_USER(HttpStatus.CONFLICT,"해당 닉네임이 이미 존재합니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
