package com.example.springbackend.Exception;

public class BoardNotFoundException extends RuntimeException {
    public BoardNotFoundException(Long id) {
        super("Could not found user with id :"+id);
    }
}
