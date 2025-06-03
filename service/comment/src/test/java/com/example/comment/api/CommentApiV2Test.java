package com.example.comment.api;


import com.example.comment.service.response.CommentPageResponse;
import com.example.comment.service.response.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

public class CommentApiV2Test {
    RestClient restClient = RestClient.create("http://localhost:9001");

    // 생성 테스트
    @Test
    void create() {
        CommentResponse response1 = create(new CommentCreateRequestV2(1L, "test1", null, 1L));
        CommentResponse response2 = create(new CommentCreateRequestV2(1L, "test2", response1.getPath(), 1L));
        CommentResponse response3 = create(new CommentCreateRequestV2(1L, "test3", response2.getPath(), 1L));

        System.out.println("response1.getCommentId() : " + response1.getCommentId());
        System.out.println("response2.getCommentId() : " + response2.getCommentId());
        System.out.println("response3.getCommentId() : " + response3.getCommentId());
    }

    CommentResponse create(CommentCreateRequestV2 request) {
        return restClient.post()
                .uri("/v2/comments")
                .body(request)
                .retrieve()
                .body(CommentResponse.class);
    }

    // 읽기 테스트
    @Test
    void read() {
        CommentResponse response = restClient.get()
                .uri("/v2/comments/{commentId}", 124313688684527064L)
                .retrieve()
                .body(CommentResponse.class);
        System.out.println("response : " + response);
    }

    @Test
    void readAll() {
        CommentPageResponse response = restClient.get()
                .uri("/v2/comments?articleId=1&pageSize=10&page=1")
                .retrieve()
                .body(CommentPageResponse.class);
        System.out.println("response : "+ response);

        for (CommentResponse comment : response.getComments()) {
            System.out.println("comment.getCommentId : " + comment.getCommentId());
        }
    }

    // 삭제 테스트
    @Test
    void delete() {
        restClient.delete()
                .uri("/v2/comments/{commentId}", 124313688684527064L)
                .retrieve();
    }

    @Getter
    @AllArgsConstructor
    public static class CommentCreateRequestV2 {
        private Long articleId;
        private String content;
        private String parentPath;
        private Long writerId;
    }
}
