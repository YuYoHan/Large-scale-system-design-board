package com.example.article.service.response;

import com.example.article.entity.Article;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleResponse {
    private Long articleId;
    private String title;
    private String content;
    private Long boardId;
    private Long writerId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static ArticleResponse from(Article article) {
        return ArticleResponse.builder()
                .articleId(article.getArticleId())
                .title(article.getTitle())
                .content(article.getContent())
                .boardId(article.getBoardId())
                .writerId(article.getWriterId())
                .createdAt(article.getCreatedAt())
                .build();


    }
}
