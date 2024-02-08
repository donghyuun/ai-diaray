package com.example.springbackend.controller;

import com.example.springbackend.DTO.BoardDto;
import com.example.springbackend.Entity.Board;
import com.example.springbackend.Service.BoardService;
import com.example.springbackend.repo.BoardRepo;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping("/board/write")
    public ResponseEntity<Object> writeBoard(@RequestBody BoardDto boardDto){

        System.out.println("/board/write PostMapping");
        Board board = new Board();
        board.setUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
        board.setTitle(boardDto.getTitle());
        board.setContent(boardDto.getContent());
        board.setCreatedDate(new Date());
        board.setModifiedDate(new Date());

        Board boardForReturn = boardService.post(board);
        System.out.println(board.getTitle());
        System.out.println(board.getContent());
        System.out.println(boardForReturn.getTitle());
        System.out.println(boardForReturn.getContent());
        // Creating HttpHeaders instance to add custom headers
        HttpHeaders headers = new HttpHeaders();
        headers.add("Custom-Header", "Value"); // Add custom header
        return ResponseEntity.ok().headers(headers).body(boardForReturn);
    }

    @GetMapping("/board")
    List<Board> getAllBoards(){
        System.out.println("/board getMapping");
        return boardService.findAll();
    }
}
