package com.example.springbackend.repo;

import com.example.springbackend.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Long> {

    Boolean existsByUsername(String username);

    //username을 받아 DB테이블에서 회원을 조회하는 메서드
    User findByUsername(String username);
}
