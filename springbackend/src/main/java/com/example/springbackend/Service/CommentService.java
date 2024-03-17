package com.example.springbackend.Service;

import com.example.springbackend.DTO.CommentDto;
import com.example.springbackend.DTO.CommentModiDto;
import com.example.springbackend.Entity.Board;
import com.example.springbackend.Entity.Comment;
import com.example.springbackend.Entity.User;
import com.example.springbackend.repo.CommentRepo;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepo commentRepo;
    private final UserService userService;
    private final BoardService boardService;

    public CommentService(CommentRepo commentRepo, UserService userService, BoardService boardService) {
        this.commentRepo = commentRepo;
        this.userService = userService;
        this.boardService = boardService;
    }

    public Comment save(CommentDto commentDto) {
        Comment comment = new Comment();
        Optional<User> user = userService.findById((long) commentDto.getUserId());
        Board board = boardService.findById(commentDto.getBoardId()).get();

        comment.setUsername(commentDto.getUsername());
        comment.setContent(commentDto.getContent());
        comment.setCreatedDate(new Date());
        comment.setModifiedDate(new Date());
        comment.setBoard(board);

        comment.setUser(user.get());
        System.out.println(comment);
        commentRepo.save(comment);
        return comment;
    }

    public List<Comment> findAllByBoardId(Long id) {
        return commentRepo.findAllByBoardId(id);
    }

    public Comment modify(CommentModiDto commentModiDto) {
        Comment comment = commentRepo.findById(commentModiDto.getCommentId()).get();

        comment.setContent(commentModiDto.getContent());
        comment.setModifiedDate(new Date());
        commentRepo.save(comment);
        System.out.println("댓글 수정 완료");
        return comment;
    }

    public void delete(long commentId){
        commentRepo.deleteById(commentId);
    }
}
