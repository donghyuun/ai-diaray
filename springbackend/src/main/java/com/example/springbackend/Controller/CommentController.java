package com.example.springbackend.Controller;

import com.example.springbackend.DTO.CommentDto;
import com.example.springbackend.DTO.CommentModiDto;
import com.example.springbackend.Entity.Board;
import com.example.springbackend.Entity.Comment;
import com.example.springbackend.Service.CommentService;
import com.example.springbackend.repo.CommentRepo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("http://localhost:3000")
public class CommentController {

    private final CommentService commentService;
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/write/comment")
    public void writeComment(@RequestBody CommentDto commentDto){
        System.out.println("/comment/write 에 post 도착");
        int boardId = commentDto.getBoardId();
        String username = commentDto.getUsername();
        int userId = commentDto.getUserId();
        String content = commentDto.getContent();

        System.out.println(boardId);
        System.out.println(username);
        System.out.println(userId);
        System.out.println(content);

        CommentDto comDto = new CommentDto();
        comDto.setBoardId(boardId);
        comDto.setUsername(username);
        comDto.setUserId(userId);
        comDto.setContent(content);
        commentService.save(commentDto);
    }

    @GetMapping("/comments/{id}")
    public List<Comment> getComments(@PathVariable("id") int boardId) {
        return commentService.findAllByBoardId((long) boardId);
    }

    @PostMapping("/modify/comment")
    public Comment modifyComment(@RequestBody CommentModiDto commentModiDto) {
        System.out.println("post /modify/comment 도착");
        System.out.println(commentModiDto.getBoardId());
        System.out.println(commentModiDto.getCommentId());
        System.out.println(commentModiDto.getContent());

        Comment modiComment = commentService.modify(commentModiDto);
        return modiComment;
    }

}
