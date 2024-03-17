package com.example.springbackend.repo;

import com.example.springbackend.Entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepo extends JpaRepository<Comment, Long> {
    public List<Comment> findAllById(Long id);

    List<Comment> findAllByBoardId(Long id);

    Optional<Comment> findById(Long commentId);

    void deleteById(Long commentId);
}
