package com.example.comment.service.response;


import com.example.comment.entity.Comment;
import com.example.comment.entity.CommentV2;
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
    private String path;
    private LocalDateTime createdAt;

    // 2depth일 때 처리
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

    // 무한 스크롤 때 처리
    public static CommentResponse from(CommentV2 comment) {
        return CommentResponse.builder()
                .commentId(comment.getCommentId())
                .content(comment.getContent())
                .path(comment.getCommentPath().getPath())
                .articleId(comment.getArticleId())
                .writerId(comment.getWriterId())
                .deleted(comment.getDeleted())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
