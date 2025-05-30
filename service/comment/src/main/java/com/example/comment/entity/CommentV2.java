package com.example.comment.entity;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

// 무한 스크롤 작업 엔티티
@Table(name = "comment_v2")
@Getter
@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentV2 {
    @Id
    private Long commentId;
    private String content;
    private Long articleId;
    private Long writerId;
    @Embedded
    private CommentPath commentPath;
    private Boolean deleted;
    private LocalDateTime createdAt;

    @Builder
    public CommentV2(Long commentId,
                     String content,
                     Long articleId,
                     Long writerId,
                     CommentPath commentPath,
                     Boolean deleted,
                     LocalDateTime createdAt) {
        this.commentId = commentId;
        this.content = content;
        this.articleId = articleId;
        this.writerId = writerId;
        this.commentPath = commentPath;
        this.deleted = deleted;
        this.createdAt = createdAt;
    }

    // 댓글 생성
    public static CommentV2 create(Long commentId,
                                   String content,
                                   Long articleId,
                                   Long writerId,
                                   CommentPath commentPath) {
        return CommentV2.builder()
                .commentId(commentId)
                .content(content)
                .articleId(articleId)
                .writerId(writerId)
                .commentPath(commentPath)
                .deleted(false)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public boolean isRoot() {
        return commentPath.isRoot();
    }

    // 소프트 삭제 상태인지 체크
    public void delete() {
        deleted = true;
    }
}
