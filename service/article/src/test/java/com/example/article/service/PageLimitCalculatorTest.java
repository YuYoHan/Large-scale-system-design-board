package com.example.article.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PageLimitCalculatorTest {
    @Test
    void calculatorTest() {
    calculatePageLimitTest(1L, 30L, 10L, 301L);
    calculatePageLimitTest(7L, 30L, 10L, 301L);
    calculatePageLimitTest(10L, 30L, 10L, 301L);
    calculatePageLimitTest(11L, 30L, 10L, 302L);
    calculatePageLimitTest(12L, 30L, 10L, 302L);
    }

    void calculatePageLimitTest(Long page, Long pageSize, Long movablePageCount, Long expected) {
        Long result = PageLimitCalculator.calculaterPageLimit(page, pageSize, movablePageCount);
        Assertions.assertThat(result).isEqualTo(expected);
    }

}