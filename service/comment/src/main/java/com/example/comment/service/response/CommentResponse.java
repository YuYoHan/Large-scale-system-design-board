package com.example.comment.service.response;


import com.example.comment.entity.Comment;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {
    private Long commentId;
    private String content;
    private Long parentCommentId;
    private Long articleId;
    private Long writerId;
    private Boolean deleted;
    private LocalDateTime createdAt;

    public static CommentResponse from(Comment comment) {
        return CommentResponse.builder()
                .commentId(comment.getCommentId())
                .content(comment.getContent())
                .parentCommentId(comment.getParentCommentId())
                .articleId(comment.getArticleId())
                .writerId(comment.getWriterId())
                .deleted(comment.getDeleted())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
