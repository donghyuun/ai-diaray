package com.example.springbackend.Entity;

import com.example.springbackend.Key.CommentId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue
    private Long id;//댓글 pk

    private String username;
    private String content; //내용
    private Date createdDate; //작성일
    private Date modifiedDate; //수정일z

    @ManyToOne
    @JoinColumn(name = "BOARD_ID") // 조인할 반대쪽 컬럼명
    private Board board;//게시글 번호 pk & fk

    @ManyToOne
    private User user;
}
