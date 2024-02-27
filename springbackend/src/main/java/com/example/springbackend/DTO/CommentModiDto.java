package com.example.springbackend.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentModiDto {
    private Long boardId;
    private Long commentId;
    private String content;
}
