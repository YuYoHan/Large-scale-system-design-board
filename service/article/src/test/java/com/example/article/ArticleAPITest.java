package com.example.article;

import com.example.article.service.response.ArticlePageResponse;
import com.example.article.service.response.ArticleResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebClient
public class ArticleAPITest {
    RestClient restClient = RestClient.create("http://localhost:9000");

    @Test
    void createTest() {
        ArticleResponse response = create(new ArticleCreateRequest(
                "hi", "my content", 1L, 1L
        ));
        System.out.println("📦 응답 내용: " + response);
    }

    @Test
    void readTest() {
        ArticleResponse response = read(184173647577194496L);
        System.out.println("📦 응답 내용: " + response);
    }

    @Test
    void  updateTest() {
        ArticleResponse response = update(184173647577194496L, new ArticleUpdateRequest("hi2", "update content"));
        System.out.println("📦 응답 내용: " + response);
    }

    @Test
    void deleteTest() {
        restClient.delete()
                .uri("/v1/articles/{articleId}", 184173647577194496L)
                .retrieve();
    }


    @Test
    void readAllTest() {
        ArticlePageResponse response = restClient.get()
                .uri("/v1/articles?boardId=1&page=1&pageSize=30")
                .retrieve()
                .body(ArticlePageResponse.class);

        System.out.println("response count : " + response.getArticleCount());
        for (ArticleResponse article : response.getArticles()) {
            System.out.println("articleId : " + article.getArticleId());
        }
    }

    @Test
    void readAllInfiniteScroll() {
        List<ArticleResponse> articleResponseList = restClient.get()
                .uri("/v1/articles/infinite-scroll?boardId=1&pageSize=5&lastArticleId=%s".formatted(184173647577194496L))
                .retrieve()
                .body(new ParameterizedTypeReference<List<ArticleResponse>>() {
                });
        System.out.println("firstPage");
        for (ArticleResponse articleResponse : articleResponseList) {
            System.out.println("article response :" + articleResponse.getArticleId());
        }
    }


    ArticleResponse update(Long articleId, ArticleUpdateRequest request) {
        return restClient.put()
                .uri("/v1/articles/{articleId}", articleId)
                .body(request)
                .retrieve()
                .body(ArticleResponse.class);
    }

    ArticleResponse read(Long articleId) {
        return restClient.get()
                .uri("/v1/articles/{articleId}", articleId)
                .retrieve()
                .body(ArticleResponse.class);
    }

    ArticleResponse create(ArticleCreateRequest request) {
        return restClient.post()
                .uri("/v1/articles")
                .body(request)
                .retrieve()
                .body(ArticleResponse.class);
    }

    @Getter
    @AllArgsConstructor
    static public class ArticleCreateRequest {
        private String title;
        private String content;
        private Long writerId;
        private Long boardId;
    }

    @Getter
    @AllArgsConstructor
    static public class ArticleUpdateRequest {
        private String title;
        private String content;
    }

}
