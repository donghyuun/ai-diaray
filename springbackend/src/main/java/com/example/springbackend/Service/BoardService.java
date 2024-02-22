package com.example.springbackend.Service;

import com.example.springbackend.Entity.Board;
import com.example.springbackend.repo.BoardRepo;
import org.springframework.stereotype.Service;

import java.util.List;
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
}
