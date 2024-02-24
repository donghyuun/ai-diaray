package com.example.springbackend.repo;

import com.example.springbackend.Entity.UserRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRefreshTokenRepo extends JpaRepository<UserRefreshToken, Long> {
    //유저의 id가 id 이면서 재발급 횟수가 count 보다 작은 UserRefreshToken 객체를 반환한다.
    UserRefreshToken findByUserIdAndReissueCountLessThan(long id, int count);

    UserRefreshToken findByUserId(long user_id);
}
