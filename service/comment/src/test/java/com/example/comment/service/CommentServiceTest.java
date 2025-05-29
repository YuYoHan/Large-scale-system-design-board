package com.example.comment.service;

import com.example.comment.entity.Comment;
import com.example.comment.repository.CommentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @InjectMocks
    CommentService commentService;

    @Mock
    CommentRepository commentRepository;

    @Test
    @DisplayName("삭제할 댓글이 자식이 있으면 삭제 표시만 한다.")
    void deleteMarkDeletedHasChildren() {
        // given
        Long articleId = 1L;
        Long commentId = 2L;
        Comment comment = createComment(articleId, commentId);
        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));
        given(commentRepository.countBy(articleId, commentId, 2L)).willReturn(2L);

        // when
        commentService.delete(commentId);

        // then
        verify(comment).delete();
    }

    @Test
    @DisplayName("하위 댓글이 삭제되고, 삭제되지 않은 부모면, 하위 댓글만 삭제")
    void 하위댓글만_삭제() {
        // given
        Long articleId = 1L;
        Long commentId = 2L;
        Long parentCommentId = 1L;

        Comment comment = createComment(articleId, commentId, parentCommentId);

        // root 댓글이 아니기 때문에 false 반환
        given(comment.isRoot()).willReturn(false);

        Comment parent = mock(Comment.class);
        // 부모는 아직 삭제되지 않아서 false 반환
        given(parent.getDeleted()).willReturn(false);

        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));
        // 하위 댓글은 삭제되니 1L을 반환
        given(commentRepository.countBy(articleId, commentId, 2L)).willReturn(1L);
        // 부모는 삭제되지 않았으니 조회서 부모 댓글 반환
        given(commentRepository.findById(parentCommentId)).willReturn(Optional.of(parent));

        // when
        commentService.delete(commentId);

        // then
        verify(commentRepository).delete(comment);
        // never()을 쓰면 호출되지 않은 것을 확인 가능
        verify(commentRepository, never()).delete(parent);
    }

    @Test
    @DisplayName("하위 댓글이 삭제되고, 삭제된 부모면, 재귀적으로 모두 삭제")
    void 하위댓글_부모댓글_재귀_삭제() {
        // given
        Long articleId = 1L;
        Long commentId = 2L;
        Long parentCommentId = 1L;

        Comment comment = createComment(articleId, commentId, parentCommentId);
        // root 댓글이 아니기 때문에 false 반환
        given(comment.isRoot()).willReturn(false);

        Comment parent = createComment(articleId, parentCommentId);
        // 부모 댓글이니 true반환
        given(parent.isRoot()).willReturn(true);
        // 부모는 삭제되어서 true 반환
        given(parent.getDeleted()).willReturn(true);

        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));
        // 하위 댓글은 삭제되니 1L을 반환
        given(commentRepository.countBy(articleId, commentId, 2L)).willReturn(1L);
        // 부모는 삭제되지 않았으니 조회서 부모 댓글 반환
        given(commentRepository.findById(parentCommentId)).willReturn(Optional.of(parent));
        given(commentRepository.countBy(articleId, parentCommentId, 2L)).willReturn(1L);

        // when
        commentService.delete(commentId);

        // then
        verify(commentRepository).delete(comment);
        // never()을 쓰면 호출되지 않은 것을 확인 가능
        verify(commentRepository).delete(parent);
    }

    private Comment createComment(Long articleId, Long commentId) {
        Comment comment = mock(Comment.class);
        given(comment.getArticleId()).willReturn(articleId);
        given(comment.getCommentId()).willReturn(commentId);
        return comment;
    }

    private Comment createComment(Long articleId, Long commentId, Long parentCommentId) {
        Comment comment = createComment(articleId, commentId);
        given(comment.getParentCommentId()).willReturn(parentCommentId);
        return comment;
    }
}