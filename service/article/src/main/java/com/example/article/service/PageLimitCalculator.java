package com.example.article.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PageLimitCalculator {
    // movablePageCount : 이동 가능한 페이지 갯수
    public static Long calculaterPageLimit(Long page, Long pageSize, Long movablePageCount) {
        return ((page -1 ) / movablePageCount) + pageSize * movablePageCount + 1;
    }
}
