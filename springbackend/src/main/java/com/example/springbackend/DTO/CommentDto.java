package com.example.springbackend.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CommentDto {
    private int boardId;
    private String username;
    private int userId;
    private String content;
}
