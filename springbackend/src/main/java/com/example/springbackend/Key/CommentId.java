    package com.example.springbackend.Key;
    import lombok.Getter;
    import lombok.Setter;
    import java.io.Serializable;

    @Getter
    @Setter
    public class CommentId implements Serializable {
        private Long board  ; // 게시글 ID
        private Long id; // 댓글 ID

        public CommentId() {
            // 기본 생성자
        }
        public CommentId(Long boardId, Long commentId) {
            this.board = boardId;
            this.id = commentId;
        }
    }
