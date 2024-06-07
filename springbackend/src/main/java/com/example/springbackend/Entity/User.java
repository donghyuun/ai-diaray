    package com.example.springbackend.Entity;


    import jakarta.persistence.*;

    import java.util.ArrayList;
    import java.util.List;

    @Entity
    public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "USER_ID")
        private Long id;
        private String name;
        private String username;
        private String password;
        private String email;
        private String role;

        @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE) //유저 삭제 시 저장된 리프레시 토큰도 삭제
        private UserRefreshToken userRefreshToken;

        @OneToMany(mappedBy = "user") // 유저 삭제해도 댓글 기록은 남겨놓아야지..
        private List<Comment> commentList = new ArrayList<>();

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

    }
