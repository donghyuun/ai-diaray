package com.example.springbackend.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JoinDto {

    private String username;
    private String name;
    private String email;
    private String password;
    private String role;
}
