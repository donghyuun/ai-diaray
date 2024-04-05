package com.example.springbackend.Exception.User;

import com.example.springbackend.Exception.ErrorCode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserApiException extends RuntimeException {

    private final ErrorCode errorCode;

}
