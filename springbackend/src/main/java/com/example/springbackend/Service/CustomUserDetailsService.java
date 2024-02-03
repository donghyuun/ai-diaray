package com.example.springbackend.Service;

import com.example.springbackend.DTO.CustomUserDetails;
import com.example.springbackend.Entity.User;
import com.example.springbackend.repo.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepo userRepository;

    public CustomUserDetailsService(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //DB에서 조회
        User userData = userRepository.findByUsername(username);

        if (userData != null) {
            //UserDetails에 담아서 return 하면 AuthenticationManager가 검증함
            return new CustomUserDetails(userData);
        }

        return null;
    }
}
