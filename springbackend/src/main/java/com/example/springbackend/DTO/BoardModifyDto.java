package com.example.springbackend.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardModifyDto {

    private String yesNoValue;
    private String title;
    private String content;
}
