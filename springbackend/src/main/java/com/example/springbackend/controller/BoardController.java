package com.example.springbackend.controller;

import com.example.springbackend.DTO.BoardDto;
import com.example.springbackend.Entity.Board;
import com.example.springbackend.repo.BoardRepo;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@CrossOrigin("http://localhost:3000")
public class BoardController {

    private final BoardRepo boardRepo;

    public BoardController(BoardRepo boardRepo) {
        this.boardRepo = boardRepo;
    }

    @PostMapping("/board/write")
    public ResponseEntity<Object> writeBoard(@RequestParam("title") String title, @RequestParam("content") String content){

        System.out.println("/board/write/ title: " + title);
        System.out.println("/board/write/ content: " + content);

        Board board = new Board();
        board.setUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        board.setTitle(title);
        board.setContent(content);
        board.setCreatedDate(new Date());
        board.setModifiedDate(new Date());

        boardRepo.save(board);
        // Creating HttpHeaders instance to add custom headers
        HttpHeaders headers = new HttpHeaders();
        headers.add("Custom-Header", "Value"); // Add custom header
        return ResponseEntity.ok().headers(headers).body(board);
    }
}
