package com.example.article.repositoy;

import com.example.article.entity.Article;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class ArticleRepositoryTest {
    @Autowired
    ArticleRepository articleRepository;

    @Test
    void findAllTest() {
        List<Article> articles = articleRepository.findAll(1L, 1499970L, 30L);
        log.debug("articles.size : {}", articles.size());

        for (Article article : articles) {
            log.debug("article : {}", article);
        }
    }

    @Test
    void countTest() {
        Long count = articleRepository.count(1L, 10000L);
        log.debug("count : {}", count);
    }
}