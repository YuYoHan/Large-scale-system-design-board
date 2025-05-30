package com.example.comment.data;

import com.example.comment.entity.Comment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kuke.board.common.snowflake.Snowflake;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class DataInitializer {
    // JPA의 EntityManager 직접 사용
    @PersistenceContext
    EntityManager entityManager;
    // 선언적 트랜잭션(@Transactional)이 아니라, 프로그래밍 방식으로 트랜잭션 처리
    @Autowired
    TransactionTemplate transactionTemplate;
    // Twitter의 ID 생성 알고리즘 기반 고유 ID 생성기
    Snowflake snowflake = new Snowflake();
    // 멀티스레드 동기화 유틸: 6000개의 작업이 전부 완료될 때까지 await()로 대기
    CountDownLatch latch = new CountDownLatch(EXECUTE_COUNT);

    static final int BULK_INSERT_SIZE = 2000;
    static final int EXECUTE_COUNT = 6000;

    @Test
    void initialize() throws InterruptedException {
        // 최대 10개의 스레드로 동시에 작업 처리
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        // 총 6000번 insert()를 비동기로 실행
        // 각 스레드가 작업을 마치면 latch.countDown() 호출 → 모든 작업 완료될 때까지 latch.await()로 대기
        for (int i = 0; i < EXECUTE_COUNT; i++) {
            executorService.submit(() -> {
                insert();
                latch.countDown();
                System.out.println("latch : " + latch.getCount());
            });
        }
        latch.await();
        executorService.shutdown();
    }

    void insert() {
        // 각 호출마다 2000개의 Article을 하나의 트랜잭션 안에서 저장
        // 예: 6000번 호출하면 총 1,200만(6000 × 2000)개의 row가 생성됨
        transactionTemplate.executeWithoutResult(status -> {
            Comment prev = null;
            for (int i = 0; i < BULK_INSERT_SIZE; i++) {
                Comment comment = Comment.create(
                        snowflake.nextId(),
                        "content " + i + "번",
                        i % 2 == 0 ? null : prev.getCommentId(),
                        1L,
                        1L
                );
                prev = comment;
                entityManager.persist(comment);
            }
        });
    }
}
