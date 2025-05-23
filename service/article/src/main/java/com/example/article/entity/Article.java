package com.example.article.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity(name = "article")
@Getter
@Table(name = "article")
@ToString
public class Article {
    @Id
    private Long articleId;
    private String title;
    private String content;
    private Long boardId;       // shard key
    private Long writerId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    // 생성
    public static Article create(Long articleId,
                                 String title,
                                 String content,
                                 Long boardId,
                                 Long writerId) {
        Article article = new Article();
        article.articleId = articleId;
        article.title = title;
        article.content = content;
        article.boardId = boardId;
        article.writerId = writerId;
        article.createdAt = LocalDateTime.now();
        article.modifiedAt = article.createdAt;
        return article;
    }

    // 수정
    public void update(String title, String content) {
        this.title =title;
        this.content = content;
        modifiedAt = LocalDateTime.now();
    }
}
