package com.example.comment.repository;

import com.example.comment.entity.Comment;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(
            value = "select count(*) from (" +
                    "   select comment_id from comment " +
                    "   where article_id = :articleId and parent_comment_id = :parentCommentId " +
                    "   limit :limit" +
                    ") t",
            nativeQuery = true
    )
    Long countBy(@Param("articleId") Long articleId,
                 @Param("parentCommentId") Long parentCommentId,
                 @Param("limit") Long limit);


    // 댓글을 articleId에 맞는 것을 대규모 트래픽일 때 페이지 처리해서 가져오기
    @Query(
            value = "select comment.comment_id, comment.content, comment.parent_comment_id, comment.article_id, " +
                    "comment.writer_id, comment.deleted, comment.created_at " +
                    "from (" +
                    "   select comment_id from comment where article_id = :articleId " +
                    "   order by parent_comment_id asc, comment_id asc " +
                    "   limit :limit offset :offset " +
                    ") t left join comment on t.comment_id = comment.comment_id",
            nativeQuery = true
    )
    List<Comment> findAll(
            @Param("articleId") Long articleId,
            @Param("offset") Long offset,
            @Param("limit") Long limit
    );

    // 댓글 count
    @Query(
            value = "select count(*) from (" +
                    "   select comment_id from comment where article_id = :article_id limit :limit" +
                    ") t",
            nativeQuery = true
    )
    Long count(@Param("article_id") Long articleId, @Param("limit") Long limit);

    // 무한 스크롤
    @Query(
            value = "select comment.comment_id, comment.content, comment.parent_comment_id, comment.article_id, " +
                    "comment.writer_id, comment.deleted, comment.created_at " +
                    "from comment " +
                    "where article_id = :articleId " +
                    "order by parent_comment_id asc, comment_id asc " +
                    "limit :limit",
            nativeQuery = true
    )
    List<Comment> findAllInfiniteScroll(
            @org.springframework.data.repository.query.Param("articleId") Long articleId,
            @org.springframework.data.repository.query.Param("limit") Long limit
    );

    // 무한 스크롤 - 마지막 부모 댓글 id를 받아서 어디에 위치하는지 파악
    @Query(
            value = "select comment.comment_id, comment.content, comment.parent_comment_id, comment.article_id, " +
                    "comment.writer_id, comment.deleted, comment.created_at " +
                    "from comment " +
                    "where article_id = :articleId and (" +
                    "   parent_comment_id > :lastParentCommentId or " +
                    "   (parent_comment_id = :lastParentCommentId and comment_id > :lastCommentId) " +
                    ")" +
                    "order by parent_comment_id asc, comment_id asc " +
                    "limit :limit",
            nativeQuery = true
    )
    List<Comment> findAllInfiniteScroll(
            @org.springframework.data.repository.query.Param("articleId") Long articleId,
            @org.springframework.data.repository.query.Param("lastParentCommentId") Long lastParentCommentId,
            @org.springframework.data.repository.query.Param("lastCommentId") Long lastCommentId,
            @org.springframework.data.repository.query.Param("limit") Long limit
    );
}
