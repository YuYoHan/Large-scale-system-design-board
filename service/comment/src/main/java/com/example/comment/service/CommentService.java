package com.example.comment.service;

import com.example.comment.entity.Comment;
import com.example.comment.repository.CommentRepository;
import com.example.comment.service.request.CommentCreateRequest;
import com.example.comment.service.response.CommentResponse;
import kuke.board.common.snowflake.Snowflake;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final Snowflake snowflake = new Snowflake();
    private final CommentRepository commentRepository;

    @Transactional
    public CommentResponse create(CommentCreateRequest request) {
        Comment parent = findParent(request);
        Comment comment = commentRepository.save(
                Comment.create(
                        snowflake.nextId(),
                        request.getContent(),
                        parent == null ? null : parent.getParentCommentId(),
                        request.getArticleId(),
                        request.getWriterId()
                )
        );
        return CommentResponse.from(comment);
    }

    private Comment findParent(CommentCreateRequest request) {
        Long parentCommentId = request.getParentCommentId();
        if(parentCommentId == null) {
            return null;
        }
        return commentRepository.findById(parentCommentId)
                .filter(Predicate.not(Comment::getDeleted)) // 1. 삭제되지 않은 댓글
                .filter(Comment::isRoot)                    // 2. 루트 댓글인지 확인
                .orElseThrow();                             // 3. 둘 다 만족하지 않으면 예외 발생
    }

    public CommentResponse read(Long commentId) {
        return CommentResponse.from(
                commentRepository.findById(commentId).orElseThrow()
        );
    }

    public void delete(Long commentId) {
        commentRepository.findById(commentId)
                .filter(Predicate.not(Comment::getDeleted))
                .ifPresent(comment -> {
                    if(hasChildren(comment)) {
                        comment.delete();
                    } else {
                        delete(comment);
                    }
                });
    }

    private boolean hasChildren(Comment comment) {
        return commentRepository.countBy(comment.getArticleId(), comment.getCommentId(), 2L) == 2;
    }

    private void delete(Comment comment) {
        commentRepository.delete(comment);

        if (!comment.isRoot()) { // 루트가 아니라면 (= 대댓글이라면)
            commentRepository.findById(comment.getParentCommentId())
                    .filter(Comment::getDeleted) // 부모가 이미 소프트 삭제 상태
                    .filter(Predicate.not(this::hasChildren)) // 자식이 더 이상 없음
                    .ifPresent(this::delete); // 부모도 실제 삭제
        }
    }
}
