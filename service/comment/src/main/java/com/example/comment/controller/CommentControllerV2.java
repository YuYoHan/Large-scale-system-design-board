package com.example.comment.controller;

import com.example.comment.service.CommentService;
import com.example.comment.service.request.CommentCreateRequest;
import com.example.comment.service.request.CommentCreateRequestV2;
import com.example.comment.service.response.CommentPageResponse;
import com.example.comment.service.response.CommentResponse;
import com.example.comment.service.response.CommentServiceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/comments")
public class CommentControllerV2 {
    private final CommentServiceV2 commentService;

    @GetMapping("/{commentId}")
    public CommentResponse read(@PathVariable Long commentId) {
        return commentService.read(commentId);
    }

    @PostMapping()
    public CommentResponse create(@RequestBody CommentCreateRequestV2 request) {
        return commentService.create(request);
    }

    @DeleteMapping("/{commentId}")
    public void delete(@PathVariable Long commentId) {
        commentService.delete(commentId);
    }

    @GetMapping("")
    public CommentPageResponse readAll(@RequestParam("articleId") Long articleId,
                                   @RequestParam("page") Long page,
                                   @RequestParam("pageSize") Long pageSize) {
        return commentService.readAll(articleId, page, pageSize);
    }

    @GetMapping("/infinite-scroll")
    public List<CommentResponse> readAllInfiniteScroll(@RequestParam("articleId") Long articleId,
                                   @RequestParam(value = "lastPath", required = false) String lastPath,
                                   @RequestParam("pageSize") Long pageSize) {
        return commentService.readAllInfiniteScroll(articleId, lastPath, pageSize);
    }



}
