package com.example.springbackend.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class Board {

    @Id
    @GeneratedValue
    @Column(name = "BOARD_ID")
    private Long id;
    private String username;
    private String title; //제목
    private String content; //내용
    private Date createdDate; //작성일
    private Date modifiedDate; //수정일
    private String imgUrl; // ai 이미지 주소

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE) //주인 아닌쪽에서 조회위해, 게시글 삭제 시 관련 댓글들도 모두 삭제
    private List<Comment> commentList = new ArrayList<>();

    //별로 안좋은 설계,, USER_ID 를 FK 로 가지는게 좋을 것 같은데 그렇게 설계안했음..

}
