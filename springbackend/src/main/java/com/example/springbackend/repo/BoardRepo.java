package com.example.springbackend.repo;

import com.example.springbackend.Entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepo extends JpaRepository<Board, Integer> {

}

