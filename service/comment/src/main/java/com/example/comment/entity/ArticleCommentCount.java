package com.example.comment.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

// 댓글 개수 카운트하는 엔티티
@Table(name = "article_comment_count")
@Entity(name = "article_comment_count")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleCommentCount {
    @Id
    private Long articleId;
    private Long commentCount;

    @Builder
    public ArticleCommentCount(Long articleId, Long commentCount) {
        this.articleId = articleId;
        this.commentCount = commentCount;
    }

    public static ArticleCommentCount init(Long articleId, Long commentCount) {
        return ArticleCommentCount.builder()
                .articleId(articleId)
                .commentCount(commentCount)
                .build();
    }
}
