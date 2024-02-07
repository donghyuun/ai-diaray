package com.example.springbackend.DTO;

import lombok.Data;
import org.aspectj.bridge.Message;
import org.springframework.http.ResponseEntity;

@Data
public class DefaultRes<T> {

    private int statusCode;
    private String resMessage;
    private T data;// 응답할 데이터 타입 제네릭 이용

    public DefaultRes(final int statusCode, final String responseMessage) {
        this.statusCode = statusCode;
        this.resMessage = responseMessage;
        this.data = null;//전달할 데이터 없으면 기본으로 null 주기
    }

    // 응답 데이터 O
//    public ResponseEntity<Message>
}
