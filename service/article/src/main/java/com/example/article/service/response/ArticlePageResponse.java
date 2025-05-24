package com.example.article.service.response;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticlePageResponse {
    private List<ArticleResponse> articles = new ArrayList<>();
    private Long articleCount;

    public static ArticlePageResponse of(List<ArticleResponse> articles, Long articleCount) {
        return ArticlePageResponse.builder()
                .articles(articles)
                .articleCount(articleCount)
                .build();
    }
}
