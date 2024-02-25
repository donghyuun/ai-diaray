package com.example.springbackend.Login;

import com.example.springbackend.DTO.CustomUserDetails;
import com.example.springbackend.Entity.User;
import com.example.springbackend.Entity.UserRefreshToken;
import com.example.springbackend.repo.UserRefreshTokenRepo;
import com.example.springbackend.repo.UserRepo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collection;
import java.util.Iterator;


public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final UserRefreshTokenRepo userRefreshTokenRepo;
    private final AuthenticationManager authenticationManager;
    private final UserRepo userRepo;

    private final JWTUtil jwtUtil;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserRefreshTokenRepo userRefreshTokenRepo, UserRepo userRepo) {

        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRefreshTokenRepo = userRefreshTokenRepo;
        this.userRepo = userRepo;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        //클라이언트 요청에서 username, password 추출
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        //스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        //token에 담은 검증을 위한 AuthenticationManager로 전달
        return authenticationManager.authenticate(authToken);
    }

    //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        //UserDetails
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        /**********token에 db의 userid넣어서 refreshtoken db에서 찾을때 쓰려고************/
        User loginUser = userRepo.findByUsername(username);
        System.out.println(loginUser);
        /***************************************************************************/

        String token = jwtUtil.createJwt(username, role, 60 * 60 * 10L, loginUser.getId());
        String refreshToken = jwtUtil.createRefreshToken(600 * 600 * 100L);

        System.out.println("token: " + token);
        response.addHeader("Access-Control-Expose-Headers", "Authorization");
        response.addHeader("Access-Control-Expose-Headers", "Refresh-Token");
        response.addHeader("Authorization", "Bearer " + token);//토큰 발급
        response.addHeader("Refresh-Token", "Bearer " + refreshToken);//리프레시 토큰 발급


        
        //기존에 해당 유저가 리프레시 토큰 가지고 있는 경우 -> 갱신
        if(userRefreshTokenRepo.findByUserId(loginUser.getId()) != null){
            userRefreshTokenRepo.findByUserId(loginUser.getId()).updateRefreshToken(refreshToken);
            System.out.println("리프레시 토큰 갱신");
        }
        //리프레시 토큰이 없는 경우 -> db에 추가
        else{
            UserRefreshToken userRefreshToken = new UserRefreshToken(loginUser, refreshToken);
            userRefreshTokenRepo.save(userRefreshToken);
            System.out.println("리프레시 토큰 추가");
        }

        
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        System.out.println("login fail");
        response.setStatus(401);
    }
}