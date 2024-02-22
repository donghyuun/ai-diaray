package com.example.springbackend.Service;

import com.example.springbackend.DTO.BoardModifyDto;
import com.example.springbackend.Entity.Board;
import com.example.springbackend.repo.BoardRepo;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BoardService {

    private final BoardRepo boardRepo;

    public BoardService(BoardRepo boardRepo) {
        this.boardRepo = boardRepo;
    }

    public Board post(Board board) {
        boardRepo.save(board);
        return board;
    }

    public List<Board> findAll() {
        return boardRepo.findAll();
    }

    public Optional<Board> findById(int id) {
        return boardRepo.findById(id);
    }

    public int deleteById(int id) {
        boardRepo.deleteById(id);
        return id;
    }

    public Board modifyBoard(int id, Board boardModifyDto, String yesNoValue) {
        Board board = boardRepo.findById(id).get();
        board.setTitle(boardModifyDto.getTitle());
        board.setContent(boardModifyDto.getContent());
        if (Objects.equals(yesNoValue, "Yes")) {
            board.setImgUrl(boardModifyDto.getImgUrl());
        }
        board.setModifiedDate(new Date());
        boardRepo.save(board);

        return board;
    }

}
