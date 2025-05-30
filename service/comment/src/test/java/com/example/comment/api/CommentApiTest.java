package com.example.comment.api;


import com.example.comment.service.response.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

public class CommentApiTest {
    RestClient restClient = RestClient.create("http://localhost:9001");

    @Test
    void create() {
        CommentResponse response1 = createComment(new CommentCreateRequest(1L, "test1", null, 1L));
        CommentResponse response2 = createComment(new CommentCreateRequest(1L, "test2", response1.getCommentId(), 1L));
        CommentResponse response3 = createComment(new CommentCreateRequest(1L, "test3", response1.getCommentId(), 1L));

        System.out.printf("commentId=%s", response1.getCommentId());
        System.out.printf("\tcommentId=%s", response2.getCommentId());
        System.out.printf("\tcommentId=%s", response3.getCommentId());
        // -> commentId=186373089862483968	commentId=186373090697150464	commentId=186373090772647936
    }

    CommentResponse createComment(CommentCreateRequest request) {
        return restClient.post()
                .uri("/v1/comments")
                .body(request)
                .retrieve()
                .body(CommentResponse.class);
    }

    @Test
    void read() {
        CommentResponse response = restClient.get()
                .uri("/v1/comments/{commentId}", 186373089862483968L)
                .retrieve()
                .body(CommentResponse.class);

        System.out.println("response : " + response);
    }

    @Test
    void delete() {
        restClient.delete()
                .uri("/v1/comments/{commentId}", 186373090772647936L)
                .retrieve();
    }

    @Getter
    @AllArgsConstructor
    public static class CommentCreateRequest {
        private Long articleId;
        private String content;
        private Long parentCommentId;
        private Long writerId;
    }
}
