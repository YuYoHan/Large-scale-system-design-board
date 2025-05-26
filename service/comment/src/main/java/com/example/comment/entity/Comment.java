package com.example.comment.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "comment")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity(name = "comment")
public class Comment {
    @Id
    private Long commentId;
    private String content;
    private Long parentCommentId;
    private Long articleId;
    private Long writerId;
    private Boolean deleted;
    private LocalDateTime createdAt;

    public static Comment create(Long commentId,
                                 String content,
                                 Long parentCommentId,
                                 Long articleId,
                                 Long writerId) {
        return Comment.builder()
                .commentId(commentId)
                .content(content)
                .parentCommentId(parentCommentId == null ? commentId : parentCommentId)
                .articleId(articleId)
                .writerId(writerId)
                .deleted(false)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public boolean isRoot() {
        return parentCommentId.longValue() == commentId;
    }

    public void delete() {
        deleted = true;
    }
}
