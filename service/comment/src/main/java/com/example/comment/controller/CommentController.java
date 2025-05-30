package com.example.comment.controller;

import com.example.comment.service.CommentService;
import com.example.comment.service.request.CommentCreateRequest;
import com.example.comment.service.response.CommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/comments")
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/{commentId}")
    public CommentResponse read(@PathVariable Long commentId) {
        return commentService.read(commentId);
    }

    @PostMapping()
    public CommentResponse create(@RequestBody CommentCreateRequest request) {
        return commentService.create(request);
    }

    @DeleteMapping("/{commentId}")
    public void delete(@PathVariable Long commentId) {
        commentService.delete(commentId);
    }

}
