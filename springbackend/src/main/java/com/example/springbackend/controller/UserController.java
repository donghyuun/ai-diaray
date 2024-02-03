package com.example.springbackend.controller;
import com.example.springbackend.Exception.UserNotFoundException;
import com.example.springbackend.Service.JoinService;
import com.example.springbackend.Entity.User;
import com.example.springbackend.model.JoinDto;
import com.example.springbackend.repo.UserRepo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")

public class UserController {

    private final JoinService joinService;
    private final UserRepo userRepository;

    public UserController(JoinService joinService, UserRepo userRepository) {
        this.joinService = joinService;
        this.userRepository = userRepository;
    }

    @PostMapping("/user")
    User newUser(@RequestBody JoinDto joinDto){
        return joinService.joinProcess(joinDto);
    }

    @GetMapping("/users")
    List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping("user/{id}")
    User getUserById(@PathVariable Long id){
        return userRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException(id));
    }

    @PutMapping("updateUser/{id}")
    User updateUser(@RequestBody User newUser,@PathVariable Long id){
        return userRepository.findById(id)
                .map(user ->{
                    user.setUsername(newUser.getUsername());
                    user.setEmail(newUser.getEmail());
                    user.setName(newUser.getName());
                    user.setPassword(newUser.getPassword());
                    user.setRole("ROLE_USER");
                    return userRepository.save(user);
                }).orElseThrow(()->new UserNotFoundException(id));
    }

    @DeleteMapping("deleteUser/{id}")
    String deleteuserById(@PathVariable Long id){
        if (!userRepository.existsById(id)){
            throw new UserNotFoundException(id);
        };
        userRepository.deleteById(id);
        return "User id"+id+"delete success";

    }




}
