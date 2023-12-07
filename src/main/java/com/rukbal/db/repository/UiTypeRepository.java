package com.rukbal.db.repository;

import com.rukbal.db.domain.UiType;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.reactive.ReactorPageableRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@R2dbcRepository(dialect = Dialect.POSTGRES)
public interface UiTypeRepository extends ReactorPageableRepository<UiType, Integer> {

    @Override
    @NonNull
    Flux<UiType> findAll();

    Mono<UiType> findByName(String name);
}
