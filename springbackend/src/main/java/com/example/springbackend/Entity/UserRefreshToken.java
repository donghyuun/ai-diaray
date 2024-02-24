package com.example.springbackend.Entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
public class UserRefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String refreshToken;

    private int reissueCount = 0;

    public UserRefreshToken(User user, String refreshToken) {//리프레시 토큰 발급한것 DB에 저장
        this.user = user;
        this.refreshToken = refreshToken;
    }

    public void updateRefreshToken(String refreshToken) {//리프레시 토큰 재발급 <-- 이것도 되는구나
        this.refreshToken = refreshToken;
    }

    public boolean validateRefreshToken(String refreshToken) {//리프레시 토큰 유효성 검사
        return this.refreshToken.equals(refreshToken);
    }
    public void increseReissueCount() {
        reissueCount++;//이 리프레시 토큰을 사용해 엑세스 토큰 재발급을 요청한 횟수
    }

}
