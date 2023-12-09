package com.rukbal.db.api;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.Sort;

public interface BaseControllerTest {
    int INT_ONE = 1;
    int DEFAULT_PAGE_SIZE = 10;
    int DEFAULT_PAGE_NUMBER = 1;
    String DEFAULT_ID_COLUMN = "id";
    @NonNull Sort SORT_BY = Sort.of(Sort.Order.asc(DEFAULT_ID_COLUMN));
    @NonNull Pageable PAGEABLE = Pageable.from(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE, SORT_BY);
}
