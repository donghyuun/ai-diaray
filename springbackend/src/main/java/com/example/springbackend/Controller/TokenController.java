package com.example.springbackend.Controller;

import com.example.springbackend.Entity.UserRefreshToken;
import com.example.springbackend.Login.JWTUtil;
import com.example.springbackend.repo.UserRefreshTokenRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;

@RestController
@CrossOrigin("http://localhost:3000")
public class TokenController {

    @Value("${spring.jwt.secret}")
    private String appName;
    @Autowired UserRefreshTokenRepo userRefreshTokenRepo;
    @Autowired JWTUtil jwtUtil;
    @GetMapping("/refresh")
    public String newAccessToken(
            @RequestHeader("Authorization") String token,
            @RequestHeader("Refresh-Token") String rfToken,
            @RequestHeader("User-Id") Long userId,
            @RequestHeader("User-Role") String userRole,
            @RequestHeader("Username") String userName,
            @org.springframework.beans.factory.annotation.Value("${spring.jwt.secret}")String secret
            )
    {
        System.out.println("엑세스토큰 만료로 리프레시 토큰이용해 발급 요청");
        //토큰 파싱
        System.out.println("token in /refresh controller: " + token);
        System.out.println("refresh token in /refresh controller: " + rfToken);
        String accessToken = token.split(" ")[1];

        //리프레시 토큰 파싱
        String refreshToken = rfToken.split(" ")[1];

        UserRefreshToken userRefreshToken = userRefreshTokenRepo.findByUserIdAndRefreshToken(userId, refreshToken);
        if (userRefreshToken != null) {
            System.out.println("db 의 리프레시 토큰이 사용자의 리프레시 토큰과 일치함");
            return jwtUtil.createJwt(userName, userRole, 60*60*100L, userId);
        }

        return "there are no same refreshToken";

    }
}

