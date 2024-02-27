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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String title; //제목
    private String content; //내용
    private Date createdDate; //작성일
    private Date modifiedDate; //수정일
    private String imgUrl; // ai 이미지 주소

//    @OneToMany(mappedBy = "board")
//    private List<Comment> commentList = new ArrayList<>();

}
