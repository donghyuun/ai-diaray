package com.example.springbackend.Service;

import com.example.springbackend.model.JoinDto;
import com.example.springbackend.Entity.User;
import com.example.springbackend.repo.UserRepo;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinService {

    private final UserRepo userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinService(UserRepo userRepository, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User joinProcess(JoinDto joinDto){
        String username = joinDto.getUsername();
        String name = joinDto.getName();
        String password = joinDto.getPassword();
        String email = joinDto.getEmail();
        String role = joinDto.getRole();

        Boolean isExist = userRepository.existsByUsername(username);

        //동일한 username의 회원이 존재할때
        if(isExist){
            return null;
        }

        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setEmail(email);
        user.setRole("ROLE_ADMIN");

        userRepository.save(user);
        return user;
    }
}
